package au.com.commbank.latency.measurement.endpoint.configuration;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import au.com.commbank.latency.measurement.configuration.LatencyMeasurementServiceConfiguration;


/**
 * Java config for the Latency Measurement Endpoint module.
 */
@Configuration
@EnableWebMvc
@Import({
    LatencyMeasurementServiceConfiguration.class,
    SwaggerApiMvcConfiguration.class
})
@ComponentScan("au.com.commbank.latency.measurement.endpoint")
public class LatencyMeasurementEndpointConfiguration extends WebMvcConfigurerAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("/swagger-ui/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(new Jaxb2RootElementHttpMessageConverter());

        final MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        converters.add(jsonConverter);
    }

    /**
     * Creates the JAXB2 marshaller/unmarshaller bean.
     * @return JAXB2 marshaller/unmarshaller bean
     */
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("au.com.commbank.platform.coding.challenge.schema.v1.dto");
        return marshaller;
    }

    /**
     * Returns a reference to the Object mapper bean which provides functionality for converting between Java
     * objects (instances of JDK provided core classes, beans), and matching JSON constructs.
     *
     * @return A reference to the Object-to-JSON mapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        return objectMapper;
    }
}
