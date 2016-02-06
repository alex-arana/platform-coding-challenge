package au.com.commbank.latency.measurement.endpoint.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import au.com.commbank.latency.measurement.model.ApplicationException;
import au.com.commbank.platform.coding.challenge.schema.v1.dto.ErrorResponseDto;


/**
 * Defines the API of a global exception handler for any application Spring MVC controllers.
 * <p>
 * This class contains {@link ExceptionHandler} annotated methods that provide a convenient means of handling
 * commons types of exceptions within a central location.
 */
@ControllerAdvice
public interface ExceptionControllerAdvice {

    /**
     * Handle the case when an unrecoverable binding exception occurs - e.g. required header, required cookie.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the exception could be rethrown as-is.
     *
     * @param ex the exception to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    ModelAndView handleServletRequestBindingException(ServletRequestBindingException ex,
        HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Handle the case when a {@link ApplicationException application exception} occurs.
     * <p>
     * Whenever an application exception is raised within a Spring MVC controller, this method handles that exception
     * and it returns the HttpStatus code associated with the underlying instance of <codeApplicationErrorDetail</code>.
     *
     * @param ex the ApplicationException to be handled
     * @param request current HTTP request
     * @return A new {@code ResponseEntity} with an instance of <code>ErrorResponseDto</code> as its body.
     */
    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationException ex, WebRequest request);

    /**
     * Handle the case when an {@link Exception} occurs.
     *
     * @param ex the Exception to be handled
     * @param request current HTTP request
     * @return A new {@code ResponseEntity} with an instance of <code>ErrorResponseDto</code> as its body.
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDto> unhandledException(Exception ex, WebRequest request);
}
