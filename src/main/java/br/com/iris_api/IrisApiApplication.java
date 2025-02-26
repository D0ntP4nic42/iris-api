package br.com.iris_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class IrisApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrisApiApplication.class, args);
	}

}
