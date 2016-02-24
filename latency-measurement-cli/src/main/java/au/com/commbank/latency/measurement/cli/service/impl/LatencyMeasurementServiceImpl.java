package au.com.commbank.latency.measurement.cli.service.impl;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public String getRoundTripLatency(final String inputFile, final Integer iterations) throws IOException {
        final Path path = Paths.get(inputFile);
        LOG.info("Performing network latency tests using input file: {}", path.toAbsolutePath());

        final List<String> lines = Files.readAllLines(path);
        final BatchLatencyMeasurementRequest request = new BatchLatencyMeasurementRequest(lines.stream()
            .map(url -> new LatencyMeasurementRequest(url, iterations))
            .collect(collectingAndThen(toList(), Collections::unmodifiableList)));

        final String url = serviceBaseUrl + "/v1/latency/batchroundtrip";
        final BatchLatencyMeasurementResponse result =
            restTemplate.postForObject(url, request, BatchLatencyMeasurementResponse.class);
        return objectMapper.writeValueAsString(result.getResponses());
    }
}
