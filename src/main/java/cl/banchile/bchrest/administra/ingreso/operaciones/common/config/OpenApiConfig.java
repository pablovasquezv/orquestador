/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.config;


import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author Pablo
 *
 */
@Configuration
@EnableSwagger2
public class OpenApiConfig {
    /** isEnabled. */
    @Value("${info.name}")
    private String apiName;
    /** apiDescription. */
    @Value("${info.description}")
    private String apiDescription;
    /** apiVersion. */
    @Value("${info.version}")
    private String apiVersion;
    /** contactName. */
    @Value("${info.contact.name}")
    private String contactName;
    /** contactEmail. */
    @Value("${info.contact.mail}")
    private String contactEmail;
    // -------------------------------------------------------------------
    // -- Métodos Públicos -----------------------------------------------
    // -------------------------------------------------------------------
    /**
     * Basic Swagger configuration.
     *
     * @return {@link Docket} Swagger configuration
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .enable(true)
                .useDefaultResponseMessages(false);
    }
    /**
     * Configures the Swagger UI:
     * - Activates/Deactivates the "TRY IT OUT" button for Swagger UI.
     *
     * @return {@link UiConfiguration} Swagger UI Configuration
     */
    @Bean
    public UiConfiguration uiConfig() {
        /*
         * - Available methods are: "get", "head", "post", "put", "delete", "connect", "options", "trace", "patch"
         * - Leave blank if you want to disable the 'TRY IT OUT' button for all methods
         */
        final String[] methodsWithTryItOutButton = new String[1];
        methodsWithTryItOutButton[0] = "post";
        return UiConfigurationBuilder.builder().supportedSubmitMethods(methodsWithTryItOutButton).build();
    }
    // -------------------------------------------------------------------
    // -- Métodos Privados -----------------------------------------------
    // -------------------------------------------------------------------
    /**
     * Sets the API information for Swagger UI.
     *
     * @return {@link ApiInfo} API Information
     */
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                this.apiName,
                this.apiDescription,
                this.apiVersion,
                null,
                new Contact(this.contactName, null, this.contactEmail),
                null,
                null,
                Collections.emptyList());
    }
}