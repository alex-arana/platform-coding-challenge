package au.com.commbank.latency.measurement.endpoint.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import au.com.commbank.latency.measurement.configuration.LatencyMeasurementServiceTestConfiguration;


/**
 * Java config for use with Latency Measurement Endpoint unit testing.
 */
@Configuration
@Import({LatencyMeasurementEndpointConfiguration.class, LatencyMeasurementServiceTestConfiguration.class})
public class LatencyMeasurementEndpointTestConfiguration {

}
