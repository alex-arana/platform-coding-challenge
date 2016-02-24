package au.com.commbank.latency.measurement.cli.service;

import java.io.IOException;


/**
 * Defines the behaviour of the Latency Measurement business service.
 */
public interface LatencyMeasurementService {

    /**
     * Returns the round-trip latency data measured for the list of URLs contained in the input file.
     *
     * @param inputFile
     *            Path to the file holding information about the round-trip latency tests to be performed
     * @param iterations
     *            Number of iterations to perform while executing a latency round-trip test. This value may be
     *            <code>null</code> in which case an internal default value will be used
     * @return JSON string containing multiple latency measurement data results
     * @throws IOException
     *             if an I/O error occurs while performing this operation
     */
    String getRoundTripLatency(String inputFile, Integer iterations) throws IOException;

}
