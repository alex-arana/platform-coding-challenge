package au.com.commbank.latency.measurement.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Charsets;
import au.com.commbank.latency.measurement.cli.service.LatencyMeasurementService;


/**
 * Command-line utility that is used to read a list of URLs from a file and then obtain
 * latency statistics by invoking the LatencyMeasurement Web Service.
 */
@SpringBootApplication
public class LatencyMeasurementClientApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(LatencyMeasurementClientApplication.class);
    private static final String DEFAULT_INPUT_FILE = "URL.txt";
    private static final String DEFAULT_OUTPUT_FILE = "latency.json";

    @Inject
    private Environment environment;

    @Inject
    private LatencyMeasurementService latencyMeasurementService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.registerModule(new JodaModule());
        return objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) throws IOException {
        final String inputFile = environment.getProperty("input", DEFAULT_INPUT_FILE);
        final String jsonString = latencyMeasurementService.getRoundTripLatency(inputFile);
        LOG.info("LatencyMeasurementService returned {}", jsonString);

        final Path outputFile = Paths.get(environment.getProperty("output", DEFAULT_OUTPUT_FILE));
        Files.write(outputFile, jsonString.getBytes(Charsets.UTF_8));
        LOG.info("Latency measurement results have been saved to file '{}'", outputFile.toAbsolutePath());
    }

    /**
     * Application entry point.
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(LatencyMeasurementClientApplication.class, args);
    }
}
