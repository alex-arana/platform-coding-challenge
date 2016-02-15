package au.com.commbank.latency.measurement.cli.service.impl;

import static java.lang.invoke.MethodHandles.lookup;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import au.com.commbank.latency.measurement.cli.ApplicationException;
import au.com.commbank.latency.measurement.cli.service.LatencyMeasurementService;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementResponse;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;


/**
 * Implementation of the LatencyMeasurement client service.
 */
@Named
public class LatencyMeasurementServiceImpl implements LatencyMeasurementService {
    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private ObjectMapper objectMapper;

    @Value("${latency.measurement.service.url}")
    private String serviceBaseUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoundTripLatency(final String inputFile) throws ApplicationException {
        try {
            final Path path = Paths.get(inputFile);
            LOG.info("Performing network latency tests using input file: {}", path.toAbsolutePath());

            final List<String> lines = Files.readAllLines(path);
            final BatchLatencyMeasurementRequest request = new BatchLatencyMeasurementRequest();
            lines.stream().forEach(line -> {
                final LatencyMeasurementRequest parameter = new LatencyMeasurementRequest();
                parameter.setTargetUrl(line);
                request.getParameters().add(parameter);
            });

            final String url = serviceBaseUrl + "/v1/latency/batchroundtrip";
            final BatchLatencyMeasurementResponse result =
                restTemplate.postForObject(url, request, BatchLatencyMeasurementResponse.class);
            return objectMapper.writeValueAsString(result);
        } catch (final IOException ex) {
            throw new ApplicationException(-2,
                "An I/O error has occured while performing this operation: " + ex.getMessage(), ex);
        }
    }
}
