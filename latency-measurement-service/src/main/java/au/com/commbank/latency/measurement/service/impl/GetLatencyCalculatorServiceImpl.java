package au.com.commbank.latency.measurement.service.impl;

import static java.lang.invoke.MethodHandles.lookup;
import java.net.URI;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Preconditions;
import au.com.commbank.latency.measurement.model.StatisticsSet;
import au.com.commbank.latency.measurement.service.LatencyCalculatorService;
import au.com.commbank.latency.measurement.util.PerformanceStopwatch;
import au.com.commbank.latency.measurement.util.UriBuilder;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * An implementation of the {@link LatencyCalculatorService} which uses a HTTP GET operation to
 * calculate the round-trip latency of a given URL.
 */
@Named
public class GetLatencyCalculatorServiceImpl implements LatencyCalculatorService {
    private static final int MAX_ACCESS_COUNT = 10000;

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

    @Inject
    private RestTemplate restTemplate;

    @Value("${latency.measurement.count}")
    private int defaultCount;

    /**
     * {@inheritDoc}
     */
    @Override
    public LatencyMeasurementResponse getRoundTripLatency(final LatencyMeasurementRequest request) {
        Preconditions.checkArgument(request != null, "Required request parameter is not present");
        final String targetUrl = request.getTargetUrl();
        Preconditions.checkArgument(StringUtils.isNotEmpty(targetUrl), "Required target URL parameter is not present");

        final URI uri = UriBuilder.create(targetUrl);
        LOG.info("Calculating round-trip latency for URI '{}'", uri);

        // validate count input parameter
        Integer count = request.getCount();
        if (count == null || count < 1 || count > MAX_ACCESS_COUNT) {
            LOG.info("specified access count value '{}' falls outside of allowable bounds ({}-{}). defaulting to '{}'",
                count, 1, MAX_ACCESS_COUNT, defaultCount);
            count = defaultCount;
        }

        final StatisticsSet statisticsSet = new StatisticsSet();
        for (int i = 0; i < count; i++) {
            final PerformanceStopwatch stopwatch = new PerformanceStopwatch(true);
            restTemplate.execute(uri, HttpMethod.GET, null, null);
            statisticsSet.add(stopwatch.getElapsedTime());
        }
        LOG.debug("latency values = {}", statisticsSet.values());

        final LatencyMeasurementResponse response = new LatencyMeasurementResponse();
        response.setUrl(uri.toString());
        response.setTimestamp(DateTime.now());
        response.setCount(statisticsSet.size());
        response.setMin(statisticsSet.getMin().orElse(null));
        response.setMax(statisticsSet.getMax().orElse(null));
        response.setRange(statisticsSet.getRange().orElse(null));
        response.setAverage(statisticsSet.getAverage().orElse(null));
        response.setMedian(statisticsSet.getMedian().orElse(null));
        response.setLatency_Ms(response.getMedian());
        return response;
    }
}
