package au.com.commbank.latency.measurement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import au.com.commbank.latency.measurement.service.impl.RestTemplateExceptionTranslator;


/**
 * Java configuration for the Latency Measurement service module.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("au.com.commbank.latency.measurement")
@PropertySource("classpath:latency-measurement-service.properties")
public class LatencyMeasurementServiceConfiguration {

    /**
     * Creates an instance of PropertySourcesPlaceholderConfigurer to allow the use of placeholders within
     * bean definition property values and <code>@Value</code> annotations against the current Spring
     * <code>Environment</code> and its set of <code>PropertySources</code>.
     *
     * @return A new instance of PropertySourcesPlaceholderConfigurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Creates an instance of Spring's RestTemplate bean which translates Spring <code>RestClientException</code>
     * exceptions to instances of <code>ApplicationException</code>.
     *
     * @return Spring RestTemplate bean which can be used in client operations
     */
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(restTemplateExceptionTranslator());
        return restTemplate;
    }

    /**
     * Translates <code>RestClientException</code>s to instances of <code>ApplicationException</code> that
     * can be more cleanly mapped to HttpStatus codes in the web service layer.
     *
     * @return A reference to the RestClientException to ApplicationException translator
     */
    @Bean
    public ResponseErrorHandler restTemplateExceptionTranslator() {
        return new RestTemplateExceptionTranslator();
    }
}
