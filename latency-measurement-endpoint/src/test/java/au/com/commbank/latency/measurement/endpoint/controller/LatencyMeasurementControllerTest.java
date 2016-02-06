package au.com.commbank.latency.measurement.endpoint.controller;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import au.com.commbank.latency.measurement.endpoint.LatencyMeasurementEndpointTest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementResponse;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * Unit test for Latency Measurement Service controller.
 */
@WebAppConfiguration
@LatencyMeasurementEndpointTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LatencyMeasurementControllerTest {
    /**
     * The embedded web server runs on this port.
     */
    public static final int WEB_SERVER_PORT = 3333;

    private static final String LATENCY_MEASUREMENT_SERVICE_URL = "/v1/latency";
    private static final Logger LOG = LoggerFactory.getLogger(LatencyMeasurementControllerTest.class);

    @Inject
    private WebApplicationContext applicationContext;

    @Inject
    private Marshaller xmlMarshaller;

    @Inject
    private Unmarshaller xmlUnmarshaller;

    @Inject
    private ObjectMapper jsonMapper;

    /**
     * Creates an embedded HTTP server that unit tests can connect to. It supports HTTP response stubbing,
     * request verification, proxy/intercept, record/playback of stubs and fault injection, and can be used
     * from within a unit test or deployed into a test environment.
     */
    @Rule
    public WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig().port(WEB_SERVER_PORT));

    private MockMvc mockMvc;

    /**
     * {@inheritDoc}
     */
    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(applicationContext).build();
    }

    @Test
    public void testGetRoundTripLatency() throws Exception {
        final LatencyMeasurementRequest request = new LatencyMeasurementRequest();
        request.setTargetUrl("www.commbank.com.au/");
        final String url = LATENCY_MEASUREMENT_SERVICE_URL + "/roundtrip";
        final StringResult stringResult = new StringResult();
        xmlMarshaller.marshal(request, stringResult);

        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_XML)
            .content(stringResult.toString()).accept(MediaType.APPLICATION_XML));
        result.andExpect(status().isOk());

        MockHttpServletResponse httpResponse = result.andReturn().getResponse();

        // assemble the response from the returned XML payload
        final StringSource source = new StringSource(httpResponse.getContentAsString());
        LatencyMeasurementResponse response = (LatencyMeasurementResponse) xmlUnmarshaller.unmarshal(source);
        assertThat("Null or empty response from getRoundTripLatency()", response, notNullValue());
        assertThat("latency_ms was null", response.getLatency_Ms(), notNullValue());

        // test JSON rendering
        result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        httpResponse = result.andReturn().getResponse();

        // assemble the response from the returned JSON payload
        final String responseString = httpResponse.getContentAsString();
        LOG.debug("REST web service returned response: {}", responseString);
        response = jsonMapper.readValue(responseString, LatencyMeasurementResponse.class);
        assertThat("Null or empty response from getRoundTripLatency()", response, notNullValue());
        assertThat("latency_ms was null", response.getLatency_Ms(), notNullValue());
    }

    @Test
    public void testGetBatchRoundTripLatency() throws Exception {
        final BatchLatencyMeasurementRequest request = new BatchLatencyMeasurementRequest();
        LatencyMeasurementRequest parameter = new LatencyMeasurementRequest();
        parameter.setTargetUrl("http://www.commbank.com.au/");
        parameter.setCount(5);
        request.getParameters().add(parameter);

        parameter = new LatencyMeasurementRequest();
        parameter.setTargetUrl("http://www.yahoo.com/");
        parameter.setCount(2);
        request.getParameters().add(parameter);

        final StringResult stringResult = new StringResult();
        xmlMarshaller.marshal(request, stringResult);

        final String url = LATENCY_MEASUREMENT_SERVICE_URL + "/batchroundtrip";
        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_XML)
            .content(stringResult.toString()).accept(MediaType.APPLICATION_XML));
        result.andExpect(status().isOk());

        MockHttpServletResponse httpResponse = result.andReturn().getResponse();

        // assemble the response from the returned XML payload
        final StringSource source = new StringSource(httpResponse.getContentAsString());
        BatchLatencyMeasurementResponse response = (BatchLatencyMeasurementResponse) xmlUnmarshaller.unmarshal(source);
        assertThat("Null or empty response from getBatchRoundTripLatency()", response, notNullValue());
        response.getResponses().forEach(it -> {
            assertThat("latency_ms was null", it.getLatency_Ms(), notNullValue());
        });

        // test JSON rendering
        result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        httpResponse = result.andReturn().getResponse();

        // assemble the response from the returned JSON payload
        final String responseString = httpResponse.getContentAsString();
        LOG.debug("REST web service returned response: {}", responseString);
        response = jsonMapper.readValue(responseString, BatchLatencyMeasurementResponse.class);
        assertThat("Null or empty response from getBatchRoundTripLatency()", response, notNullValue());
        response.getResponses().forEach(it -> {
            assertThat("latency_ms was null", it.getLatency_Ms(), notNullValue());
        });
    }
}
