package tn.esprit.easyfund.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KonnectConfig {
    @Bean
    public String apiKey() {
        return "660a81894a1f5436d6f82039:FXFKXmyj1rfQ1Drh";
    }

    @Bean
    public String walletId() {
        return "660a81894a1f5436d6f8203d";
    }
}
