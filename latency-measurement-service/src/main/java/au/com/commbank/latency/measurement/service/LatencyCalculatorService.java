package au.com.commbank.latency.measurement.service;

import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * Business service used to obtain URL latency timings.
 */
public interface LatencyCalculatorService {

    /**
     * Calculates the network latency involved in accessing the specified URL over the specified
     * number of iterations.
     *
     * @param request Data Transfer object containing request parameter data including target URL
     *        and number of iterations.
     */
    LatencyMeasurementResponse getRoundTripLatency(LatencyMeasurementRequest request);

}
