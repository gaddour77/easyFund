package tn.esprit.easyfund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class EasyFundApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFundApplication.class, args);
    }

}
