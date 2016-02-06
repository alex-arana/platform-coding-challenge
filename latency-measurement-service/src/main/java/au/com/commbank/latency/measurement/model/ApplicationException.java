package au.com.commbank.latency.measurement.model;

import lombok.Getter;

/**
 * Base class for all exceptions raised internally to signal that an error condition has occurred.
 */
@Getter
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final ApplicationErrorDetail errorDetail;

    /**
     * Creates a new instance of <code>ApplicationException</code> using the specified <code>exceptionInfo</code>
     * parameter.
     *
     * @param exceptionInfo Contains details about the error for which the exception is to be raised
     */
    public ApplicationException(final ApplicationErrorDetail errorDetail) {
		this(errorDetail, null);
    }

    /**
     * Creates a new instance of <code>ApplicationException</code> using the specified <code>exceptionInfo</code>
     * parameter.
     *
     * @param exceptionInfo Contains details about the error for which the exception is to be raised
     */
    public ApplicationException(final ApplicationErrorDetail errorDetail, final Exception cause) {
        this(errorDetail, errorDetail.getMessage(), cause);
    }

    /**
     * Creates a new instance of <code>ApplicationException</code> using the specified <code>exceptionInfo</code>
     * parameter.
     *
     * @param exceptionInfo Contains details about the error for which the exception is to be raised
     */
    public ApplicationException(final ApplicationErrorDetail errorDetail, final String message, final Exception cause) {
        super(message, cause);
        this.errorDetail = errorDetail;
    }
}
