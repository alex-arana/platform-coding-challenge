package au.com.commbank.latency.measurement.endpoint.configuration;

import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.collect.ImmutableList;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.configuration.SwaggerGlobalSettings;
import com.mangofactory.swagger.controllers.DefaultSwaggerController;
import com.mangofactory.swagger.core.SwaggerApiResourceListing;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.scanners.ApiListingReferenceScanner;


/**
 * Swagger API Spring MVC Configuration.
 */
@Configuration
@ComponentScan({ "com.mangofactory.swagger.controllers", "com.mangofactory.swagger.configuration" })
public class SwaggerApiMvcConfiguration {
    private static final List<String> DEFAULT_INCLUDE_PATTERNS = ImmutableList.of("/v1/latency.*");
    private static final String SWAGGER_GROUP = "rest-api";

    /**
     * Autowire the bundled swagger config.
     */
    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    /**
     * Global swagger settings.
     * @return Global swagger settings
     */
    @Bean
    public SwaggerGlobalSettings swaggerGlobalSettings() {
        final SwaggerGlobalSettings swaggerGlobalSettings = new SwaggerGlobalSettings();
        swaggerGlobalSettings.setGlobalResponseMessages(springSwaggerConfig.defaultResponseMessages());

        // This is where we add types to ignore (or use the default provided types)
        swaggerGlobalSettings.setIgnorableParameterTypes(springSwaggerConfig.defaultIgnorableParameterTypes());

        // This is where we add type substitutions (or use the default provided alternates)
        swaggerGlobalSettings.setAlternateTypeProvider(springSwaggerConfig.defaultAlternateTypeProvider());
        return swaggerGlobalSettings;
    }

    /**
     * API Info as it appears on the swagger-ui page.
     * @return API Info
     */
    private ApiInfo apiInfo() {
        final ApiInfo apiInfo = new ApiInfo(
            "LatencyMeasurementService REST API",
            "LatencyMeasurementService Spring MVC Swagger 1.2 API",
            "http://en.wikipedia.org/wiki/Terms_of_service",
            "no-reply@commbank.com.au",
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0.html");
        return apiInfo;
    }

    /**
     * Configure a SwaggerApiResourceListing for each swagger instance within your application.
     * e.g. 1. private  2. external apis.
     * <p>
     * Required to be a spring bean as spring will call the postConstruct method to bootstrap swagger scanning.
     *
     * @return Swagger API resource listing
     */
    @Bean
    public SwaggerApiResourceListing swaggerApiResourceListing() {
        // The group name is important and should match the group set on ApiListingReferenceScanner
        // Note that swaggerCache() is by DefaultSwaggerController to serve the swagger json
        final SwaggerApiResourceListing swaggerApiResourceListing =
            new SwaggerApiResourceListing(springSwaggerConfig.swaggerCache(), SWAGGER_GROUP);

        // Set the required swagger settings
        swaggerApiResourceListing.setSwaggerGlobalSettings(swaggerGlobalSettings());

        // Use a custom path provider or springSwaggerConfig.defaultSwaggerPathProvider()
        //swaggerApiResourceListing.setSwaggerPathProvider(springSwaggerConfig.defaultSwaggerPathProvider());
        swaggerApiResourceListing.setSwaggerPathProvider(applicationPathProvider());

        // Supply the API Info as it should appear on swagger-ui web page
        swaggerApiResourceListing.setApiInfo(apiInfo());

        // Global authorization - see the swagger documentation
        //swaggerApiResourceListing.setAuthorizationTypes(authorizationTypes());

        // Sets up an auth context - i.e. which controller request paths to apply global auth to
        //swaggerApiResourceListing.setAuthorizationContext(authorizationContext());

        // Every SwaggerApiResourceListing needs an ApiListingReferenceScanner to scan the spring request mappings
        swaggerApiResourceListing.setApiListingReferenceScanner(apiListingReferenceScanner());
        return swaggerApiResourceListing;
    }

    /**
     * The ApiListingReferenceScanner does most of the work.
     * Scans the appropriate spring RequestMappingHandlerMappings
     * Applies the correct absolute paths to the generated swagger resources
     *
     * @return API listing scanner
     */
    @Bean
    public ApiListingReferenceScanner apiListingReferenceScanner() {
        final ApiListingReferenceScanner apiListingReferenceScanner = new ApiListingReferenceScanner();

        // Picks up all of the registered spring RequestMappingHandlerMappings for scanning
        apiListingReferenceScanner.setRequestMappingHandlerMapping(
            springSwaggerConfig.swaggerRequestMappingHandlerMappings());

        // Excludes any controllers with the supplied annotations
        apiListingReferenceScanner.setExcludeAnnotations(springSwaggerConfig.defaultExcludeAnnotations());

        // How to group request mappings to ApiResource's typically by spring controller clesses or @Api.value()
        apiListingReferenceScanner.setResourceGroupingStrategy(springSwaggerConfig.defaultResourceGroupingStrategy());

        // Path provider used to generate the appropriate uri's
        //apiListingReferenceScanner.setSwaggerPathProvider(springSwaggerConfig.defaultSwaggerPathProvider());
        apiListingReferenceScanner.setSwaggerPathProvider(applicationPathProvider());

        // Must match the swagger group set on the SwaggerApiResourceListing
        apiListingReferenceScanner.setSwaggerGroup(SWAGGER_GROUP);

        // Only include paths that match the supplied regular expressions
        apiListingReferenceScanner.setIncludePatterns(DEFAULT_INCLUDE_PATTERNS);

        return apiListingReferenceScanner;
    }

    /**
     * Returns a reference to the LatencyMeasurementService custom path provider bean.
     * @return a reference to the LatencyMeasurementService custom path provider bean.
     */
    @Bean
    public SwaggerPathProvider applicationPathProvider() {
        return new LatencyMeasurementServicePathProvider();
    }

    /**
     * Customises the default SwaggerUI base path resolution algorithm.
     */
    static class LatencyMeasurementServicePathProvider extends SwaggerPathProvider {
        @Inject
        private Environment environment;

        @Inject
        private ServletContext servletContext;

        private String getHostname() {
            return environment.getProperty("swagger-api.hostname", "localhost");
        }

        private String getPort() {
            return environment.getProperty("swagger-api.port", "8180");
        }

        private String getProtocol() {
            return environment.getProperty("swagger-api.protocol", "http");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String applicationPath() {
            return UriComponentsBuilder
                .fromHttpUrl(String.format("%s://%s:%s", getProtocol(), getHostname(), getPort()))
                .path(servletContext.getContextPath())
                .build()
                .toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getDocumentationPath() {
            return UriComponentsBuilder
                .fromHttpUrl(String.format("%s://%s:%s", getProtocol(), getHostname(), getPort()))
                .path(DefaultSwaggerController.DOCUMENTATION_BASE_PATH)
                .build()
                .toString();
        }
    }
}
