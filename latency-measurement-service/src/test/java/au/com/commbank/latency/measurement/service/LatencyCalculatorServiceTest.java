package au.com.commbank.latency.measurement.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import au.com.commbank.latency.measurement.LatencyMeasurementServiceTest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * Unit test for {@link LatencyCalculatorService}.
 */
@LatencyMeasurementServiceTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LatencyCalculatorServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(LatencyCalculatorServiceTest.class);

    @Inject
    private LatencyCalculatorService latencyCalculatorService;

    @Test
    public void testGetRoundTripLatency() throws Exception {
        final LatencyMeasurementRequest request = new LatencyMeasurementRequest();
        request.setTargetUrl("http://www.commbank.com.au/");

        final LatencyMeasurementResponse response = latencyCalculatorService.getRoundTripLatency(request);
        LOG.info(response.toString());

        assertThat(response, notNullValue());
        assertThat(response.getUrl(), equalTo(request.getTargetUrl()));
        assertThat(response.getCount(), equalTo(3));
    }

    @Test
    public void testGetRoundTripLatencyWithIterations() throws Exception {
        final LatencyMeasurementRequest request = new LatencyMeasurementRequest();
        request.setTargetUrl("http://www.commbank.com.au/");
        request.setCount(5);

        final LatencyMeasurementResponse response = latencyCalculatorService.getRoundTripLatency(request);
        LOG.info(response.toString());

        assertThat(response, notNullValue());
        assertThat(response.getUrl(), equalTo(request.getTargetUrl()));
        assertThat(response.getCount(), equalTo(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoundTripLatencyWithMissingRequest() throws Exception {
        latencyCalculatorService.getRoundTripLatency(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoundTripLatencyWithMissingTargetUrl() throws Exception {
        final LatencyMeasurementRequest request = new LatencyMeasurementRequest();
        request.setCount(5);
        latencyCalculatorService.getRoundTripLatency(request);
    }
}
