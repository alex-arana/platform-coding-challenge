package au.com.commbank.latency.measurement.cli;

import org.springframework.boot.ExitCodeGenerator;


/**
 * Exception raised internally to signal that an error condition has occurred.
 */
public class ApplicationException extends RuntimeException implements ExitCodeGenerator {
    private static final long serialVersionUID = 8698028939525159173L;
    private final int exitCode;

    /**
     * Constructs a new instance of <code>ApplicationException</code> using the specified input parameters.
     *
     * @param exitCode The exit code that should be returned from the application.
     * @param message The detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param cause The cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).
     */
    public ApplicationException(final int exitCode, final String message, final Throwable cause) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExitCode() {
       return exitCode;
    }
}
