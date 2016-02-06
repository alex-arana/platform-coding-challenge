package au.com.commbank.latency.measurement.util;

import static java.lang.invoke.MethodHandles.lookup;
import java.net.URI;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import au.com.commbank.latency.measurement.model.ApplicationException;


/**
 * Utility class used to build URI objects from their full or partial String representation.
 */
public final class UriBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private final static String HTTP_SCHEME = "http";
    private final static String HTTPS_SCHEME = "https";
    private final static String DEFAULT_SCHEME_PREFIX = HTTP_SCHEME + "://";

    private static final Pattern URL_PATTERN =
        Pattern.compile("\\b(https?://)(www\\.)?.+", Pattern.CASE_INSENSITIVE);

    /**
     * This static list of HSTS hosts is here purely to simulate browser behaviour which allows it to
     * know when to use HTTPS to always access certain hosts..
     * For additional details regarding HTTP Strict Transport Security (HSTS) refer to the following
     * <a href="http://www.troyhunt.com/2015/06/understanding-http-strict-transport.html">online article</a>.
     */
    private static final Set<String> HSTS_PRELOAD_LIST = ImmutableSet.<String>builder()
        .add("facebook.com")
        .add("google.com")
        .add("paypal.com")
        .add("twitter.com")
        .build();

    /**
     * {@link Predicate} use to test whether or not a given host is in the preloaded list of HSTS hosts.
     */
    private static final Predicate<String> IS_HSTS_HOST = host -> HSTS_PRELOAD_LIST.stream()
        .filter(it -> StringUtils.contains(host, it))
        .findFirst()
        .isPresent();

    /**
     * Prevent public instantiation of this helper class.
     */
    private UriBuilder() {

    }

    /**
     * Factory method used to create an instance of {@link URI} from the given specification String.
     * <p>
     * The implementation of this method parses the input String into a URI and if no HTTP scheme is
     * detected it is then defaulted to {@value #HTTP_SCHEME}.
     *
     * @param spec The {@code String} to parse as a URI.
     * @return The new URI instance
     * @throws ApplicationException if a problem occurs parsing the input spec into a new URI instance
     */
    public static URI create(final String spec) {
        Preconditions.checkArgument(StringUtils.isNotBlank(spec), "URI spec cannot be blank in call to create().");

        // if scheme is missing from spec use HTTP by default
        URI uri = null;
        if (URL_PATTERN.matcher(spec).matches()) {
            uri = URI.create(spec);
        } else {
            uri = URI.create(DEFAULT_SCHEME_PREFIX + spec);
        }

        // if this given host is in the preloaded list of HSTS hosts override the scheme to use HTTPS
        if (IS_HSTS_HOST.test(uri.getHost()) && !StringUtils.equals(HTTPS_SCHEME, uri.getScheme())) {
            uri = javax.ws.rs.core.UriBuilder.fromUri(uri).scheme(HTTPS_SCHEME).build();
        }

        LOG.debug("Parsed input URI specification to URI '{}'", uri);
        return uri;
    }
}
