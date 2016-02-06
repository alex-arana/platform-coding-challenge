package au.com.commbank.latency.measurement.endpoint.controller.impl;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import au.com.commbank.latency.measurement.endpoint.controller.ExceptionControllerAdvice;
import au.com.commbank.latency.measurement.model.ApplicationErrorDetail;
import au.com.commbank.latency.measurement.model.ApplicationException;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.ErrorResponseDto;


/**
 * Default implementation of {@link ExceptionControllerAdvice}. Generic exception handler
 * for all 'advised' Spring MVC controllers.
 */
@ControllerAdvice
public class DefaultExceptionControllerAdvice implements ExceptionControllerAdvice, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionControllerAdvice.class);

    @Inject
    private ApplicationContext applicationContext;

    /**
     * Corresponds to the order by which this advice is to be processed by Exception Handlers.
     * The lower the number, the higher the priority is.
     */
    private int order = Ordered.HIGHEST_PRECEDENCE + 1000;

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelAndView handleServletRequestBindingException(final ServletRequestBindingException ex,
        final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final String errorMessage = "An unrecoverable binding exception has occured: " + ex.getMessage();
        LOG.error(errorMessage, ex);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
        return new ModelAndView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ErrorResponseDto> handleApplicationException(
        final ApplicationException ex, final WebRequest request) {

        return createResponseEntity(ex.getErrorDetail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ErrorResponseDto> unhandledException(final Exception ex, final WebRequest request) {
        Throwable cause = ExceptionUtils.getRootCause(ex);
        if (cause == null) {
            cause = ex;
        } else if (cause instanceof ApplicationException) {
            return handleApplicationException((ApplicationException) cause, request);
        }

        final String message = cause.getMessage();
        LOG.error(message, ex);
        return createResponseEntity(ApplicationErrorDetail.CORE_GENERIC_EXCEPTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order of this advice in the exception handling chain.
     * @param order an order by which this advice is to be processed by Exception Handlers.
     * The lower the number, the higher the priority is.
     */
    public void setOrder(final int order) {
        this.order = order;
    }

    /**
     * Creates a new instance of {@link ResponseEntity} from the given <code>exceptionInfo</code>.
     *
     * @param errorDetail Object containing error/status information stored in an application-specific format.
     * @return a new instance of ResponseEntity containing the specified exception detail as its payload
     */
    public ResponseEntity<ErrorResponseDto> createResponseEntity(final ApplicationErrorDetail errorDetail) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        final HttpStatus httpStatus = HttpStatus.valueOf(errorDetail.getHttpStatus().value());

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setModule(getModuleName());
        errorResponseDto.setCode(errorDetail.getCode());
        errorResponseDto.setHttpStatus(errorDetail.getHttpStatus().value());
        errorResponseDto.setReasonPhrase(errorDetail.getHttpStatus().name());
        errorResponseDto.setMessage(errorDetail.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponseDto, httpHeaders, httpStatus);
    }

    /**
     * Returns a name for the deployed application that this context belongs to.
     * <p>
     * The name String returned by Spring may well be <code>null</code>.
     *
     * @return name of the deployed application that this context belongs to
     */
    private String getModuleName() {
        return StringUtils.remove(applicationContext.getApplicationName(), "/");
    }
}
