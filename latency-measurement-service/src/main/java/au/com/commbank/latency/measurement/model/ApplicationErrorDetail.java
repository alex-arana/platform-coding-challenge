package au.com.commbank.latency.measurement.model;

import java.io.Serializable;
import java.text.MessageFormat;
import org.springframework.http.HttpStatus;


/**
 * Encapsulates error details including things like error code, error message and HTTP status.
 */
public final class ApplicationErrorDetail implements Serializable {
    private static final long serialVersionUID = 2562444284033557844L;

    /**
     * Request has invalid or missing parameters.
     */
    public static final ApplicationErrorDetail INVALID_REQUEST_PARAMETERS =
        new ApplicationErrorDetail("INVALID_REQUEST_PARAMETERS",
            "Request has invalid or missing parameters.", HttpStatus.BAD_REQUEST);

    /**
     * Remote URL returned an invalid response.
     */
    public static final ApplicationErrorDetail REMOTE_URL_INVALID_RESPONSE =
        new ApplicationErrorDetail("REMOTE_URL_INVALID_RESPONSE",
            "Remote URL returned an invalid response.");

    /**
     * A configuration exception has occurred.
     */
    public static final ApplicationErrorDetail CONFIGURATION_EXCEPTION =
        new ApplicationErrorDetail("CONFIGURATION_EXCEPTION", "A configuration error has occurred.");

    /**
     * Generic catch-all error info.
     */
    public static final ApplicationErrorDetail CORE_GENERIC_EXCEPTION =
        new ApplicationErrorDetail("CORE_GENERIC_EXCEPTION", "An unexpected eror has occured.");

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * Constructs a new instance of <code>ApplicationErrorDetail</code> from the specified <code>code</code>
     * and <code>message</code> parameters. This constructor will default the {@link HttpStatus} field value to
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     *
     * @param code unique application code for the underlying error
     * @param message argument containing a human-readable description of the underlying error.
     */
    private ApplicationErrorDetail(final String code, final String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new instance of <code>ApplicationErrorDetail</code> from the specified <code>code</code>,
     * <code>message</code> and <code>httpStatus</code> parameters.
     *
     * @param code unique application code for the underlying error
     * @param message argument containing a human-readable description of the underlying error.
     * @param httpStatus the HTTP status code corresponding to this exception detail
     */
    private ApplicationErrorDetail(final String code, final String message, final HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0}: {1}", code, message);
    }
}
