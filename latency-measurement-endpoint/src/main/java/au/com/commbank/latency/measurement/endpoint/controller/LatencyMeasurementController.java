package au.com.commbank.latency.measurement.endpoint.controller;

import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.BatchLatencyMeasurementResponse;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementRequest;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.LatencyMeasurementResponse;


/**
 * Defines the API of the Latency Measurement REST Web Service.
 */
public interface LatencyMeasurementController {

    /**
     * Returns the round-trip latency data measured for the URL contained in the input request.
     *
     * @param request Data structure holding information about the round-trip latency test to be performed
     * @return Data Transfer Object containing latency measurement data results
     */
    LatencyMeasurementResponse getRoundTripLatency(LatencyMeasurementRequest request);

    /**
     * Returns the round-trip latency data measured for the URLs contained in the input request.
     * <p>
     * This method differs from {@link #getRoundTripLatency(LatencyMeasurementRequest)} in that it can be
     * used to obtain the round-trip for several URLs simultaneously as a batch operation.
     *
     * @param request Data structure holding information about the round-trip latency tests to be performed
     * @return Data Transfer Object containing multiple latency measurement data results
     */
    BatchLatencyMeasurementResponse getBatchRoundTripLatency(BatchLatencyMeasurementRequest request);

}
