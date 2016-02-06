package au.com.commbank.latency.measurement.configuration;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.mockito.Matchers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


/**
 * Java config for use with Latency Measurement Service unit testing.
 */
@Configuration
@Import(LatencyMeasurementServiceConfiguration.class)
public class LatencyMeasurementServiceTestConfiguration {
    private static final int LOW = 0;
    private static final int HIGH = 100;

    private final Random random = new Random();

    /**
     * Overrides the RestTemplate bean for use with unit tests.
     *
     * @return Spring RestTemplate mock which can be used in unit tests
     */
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = mock(RestTemplate.class);

        // sleep a random value between 0-99 ms whenever this particular
        // flavour of the execute method is invoked on the mock
        doAnswer(invocation -> {
            final int thinkTime = random.nextInt(HIGH - LOW) + LOW;
            TimeUnit.MILLISECONDS.sleep(thinkTime);
            return null;
        }).when(restTemplate).execute(
            Matchers.any(URI.class),
            Matchers.eq(HttpMethod.GET),
            Matchers.eq(null),
            Matchers.eq(null));

        return restTemplate;
    }
}
