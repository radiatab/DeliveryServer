package fr.TabetSaidi.tp1Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Tp1BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp1BackendApplication.class, args);
	}

}
