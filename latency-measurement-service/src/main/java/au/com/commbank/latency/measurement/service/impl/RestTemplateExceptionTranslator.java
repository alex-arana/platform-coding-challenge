package au.com.commbank.latency.measurement.service.impl;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import au.com.commbank.latency.measurement.model.ApplicationErrorDetail;
import au.com.commbank.latency.measurement.model.ApplicationException;


/**
 * Translates {@link RestClientException}s to instances of {@link ApplicationException} that can be more
 * cleanly handled in the web service layer.
 */
public class RestTemplateExceptionTranslator extends DefaultResponseErrorHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (final RestClientException ex) {
            throw new ApplicationException(
                ApplicationErrorDetail.REMOTE_URL_INVALID_RESPONSE, ex.getMessage(), ex);
        }
    }
}
