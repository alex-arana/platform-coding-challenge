package au.com.commbank.latency.measurement.cli.service;

import java.io.IOException;


/**
 * Defines the behaviour of the Latency Measurement business service.
 */
public interface LatencyMeasurementService {

    /**
     * Returns the round-trip latency data measured for the list of URLs contained in the input file.
     *
     * @param inputFile Path to the file holding information about the round-trip latency tests to be performed
     * @return JSON string containing multiple latency measurement data results
     * @throws IOException if an I/O error occurs while performing this operation
     */
    String getRoundTripLatency(String inputFile) throws IOException;

}
