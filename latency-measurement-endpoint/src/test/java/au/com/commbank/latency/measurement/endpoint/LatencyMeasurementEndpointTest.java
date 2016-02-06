package au.com.commbank.latency.measurement.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.ContextConfiguration;
import au.com.commbank.latency.measurement.endpoint.configuration.LatencyMeasurementEndpointTestConfiguration;


/**
 * Meta-annotation to be used by all unit tests used in this module.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = LatencyMeasurementEndpointTestConfiguration.class)
public @interface LatencyMeasurementEndpointTest {

}
