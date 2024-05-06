package tn.esprit.easyfund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories("tn.esprit.easyfund.repositories")
@EntityScan("tn.esprit.easyfund.entities")
public class EasyFundApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFundApplication.class, args);
    }

}
