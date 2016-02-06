package au.com.commbank.latency.measurement.cli;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import au.com.commbank.latency.measurement.cli.service.LatencyMeasurementService;


/**
 * Command-line utility that is used to read a list of URLs from a file and then obtain
 * latency statistics by invoking the LatencyMeasurement Web Service.
 */
@SpringBootApplication
public class LatencyMeasurementClientApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(LatencyMeasurementClientApplication.class);
    private static final String DEFAULT_INPUT_FILE = "URL.txt";

    @Inject
    private LatencyMeasurementService latencyMeasurementService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JodaModule());
        return objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) {
        String inputFile = DEFAULT_INPUT_FILE;
        if (args.length > 1 && args[0].equals("input")) {
            inputFile = args[1];
        }

        final String jsonString = latencyMeasurementService.getRoundTripLatency(inputFile);
        LOG.info("LatencyMeasurementService returned {}", jsonString);
    }

    /**
     * Application entry point.
     */
    public static void main(final String[] args) throws Exception {
        final SpringApplication application = new SpringApplication(LatencyMeasurementClientApplication.class);
        application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
        SpringApplication.run(LatencyMeasurementClientApplication.class, args);
    }
}
