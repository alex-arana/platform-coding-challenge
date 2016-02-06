package au.com.commbank.latency.measurement.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_XML;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import au.com.commbank.latency.measurement.model.ApplicationException;
import au.com.commbank.latency.measurement.service.impl.RestTemplateExceptionTranslator;


/**
 * Unit test for {@link RestTemplateExceptionTranslator}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RestTemplateExceptionTranslatorTest {
    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateExceptionTranslatorTest.class);

    @Mock
    private ClientHttpResponse httpResponse;

    private RestTemplateExceptionTranslator restExceptionTranslator;

    @Before
    public void setUp() {
        restExceptionTranslator = new RestTemplateExceptionTranslator();
    }

    /**
     * Unit test case for {@link RestTemplateExceptionTranslator#handleError(ClientHttpResponse)}.
     */
    @Test
    public void testHandleErrorResponse() throws IOException {
        for (final HttpStatus httpStatus : HttpStatus.values()) {
            try {
                reset(httpResponse);
                final HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(APPLICATION_XML);
                final String reasonPhrase = httpStatus.getReasonPhrase();
                when(httpResponse.getStatusCode()).thenReturn(httpStatus);
                when(httpResponse.getStatusText()).thenReturn(reasonPhrase);
                when(httpResponse.getHeaders()).thenReturn(httpHeaders);
                when(httpResponse.getBody()).thenReturn(new ByteArrayInputStream(reasonPhrase.getBytes()));
                restExceptionTranslator.handleError(httpResponse);
                fail("Expected ApplicationException to be raised.");
            } catch (final ApplicationException ex) {
                LOG.debug("Expected exception was transalated to: " + ex.getErrorDetail());
            }
        }
    }
}
