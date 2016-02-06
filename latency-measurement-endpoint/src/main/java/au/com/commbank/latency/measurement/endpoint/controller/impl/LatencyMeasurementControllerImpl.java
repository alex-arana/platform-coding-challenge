package au.com.commbank.latency.measurement.endpoint.controller.impl;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import au.com.commbank.latency.measurement.endpoint.controller.LatencyMeasurementController;
import au.com.commbank.latency.measurement.service.LatencyCalculatorService;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementResponse;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * Spring MVC Controller for "Latency Measurement Service Endpoint".
 */
@RestController
@RequestMapping(value = "/v1/latency", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Api(value = "Latency Measurement Service Operations", description = "Latency Measurement REST API") // Swagger annotation
public class LatencyMeasurementControllerImpl implements LatencyMeasurementController {
    private static final Logger LOG = LoggerFactory.getLogger(LatencyMeasurementControllerImpl.class);

    /**
     * Whether or not run all latency measurement operations concurrently when executing a batch operation.
     */
    @Value("${latency.measurement.concurrent:true}")
    private boolean useParallelism;

    /**
     * A reference to the Latency Calculator business service delegate.
     */
    @Inject
    private LatencyCalculatorService latencyCalculatorService;

    /**
     * {@inheritDoc}
     */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/roundtrip", method = RequestMethod.POST)
    @ApiOperation(value = "Calculate Latency", notes = "Calculate Network Latency using specified request parameters.")
    public LatencyMeasurementResponse getRoundTripLatency(
        @ApiParam(required = true, value = "Network latency parameters including URL and count.")
        @RequestBody(required = true) final LatencyMeasurementRequest request) {

        LOG.info("getRoundTripLatency() invoked using the following parameters: {}", request);
        return latencyCalculatorService.getRoundTripLatency(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/batchroundtrip", method = RequestMethod.POST)
    @ApiOperation(value = "Batch operation for Calculate Latency",
        notes = "Calculate Network Latency as a batch operation using specified request parameters.")
    public BatchLatencyMeasurementResponse getBatchRoundTripLatency(
        @ApiParam(required = true, value = "Network latency parameters including URL and count.")
        @RequestBody(required = true) final BatchLatencyMeasurementRequest request) {

        LOG.info("getBatchRoundTripLatency() invoked using the following parameters: {}", request);
        final BatchLatencyMeasurementResponse response = new BatchLatencyMeasurementResponse();
        final List<LatencyMeasurementRequest> requestParameters = request.getParameters();
        if (CollectionUtils.isNotEmpty(requestParameters)) {
            final List<LatencyMeasurementResponse> responses = response.getResponses();
            if (useParallelism) {
                requestParameters.stream().parallel().forEach(it -> responses.add(getRoundTripLatency(it)));
            } else {
                requestParameters.stream().forEach(it -> responses.add(getRoundTripLatency(it)));
            }
        }

        LOG.debug("getBatchRoundTripLatency() returned the following response(s): {}", response);
        return response;
    }
}
