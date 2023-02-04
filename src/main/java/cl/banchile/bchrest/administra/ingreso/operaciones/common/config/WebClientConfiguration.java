/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
/**
 * @author Pablo
 *
 */

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient defaultWebClient(WebClient.Builder webClientBuilder){
        return webClientBuilder
                .clone()
                .build();
    }
}