package ma.enset.productsapp.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    // Keycloak default configuration is in Keycloak.json File
    // please search your configuration from application.properties not from Json File(like Angular app)
    public KeycloakSpringBootConfigResolver configResolver(){

        return  new KeycloakSpringBootConfigResolver();
    }

    // or we could just ignore this class and put the File Json configuration in resource folder

}
