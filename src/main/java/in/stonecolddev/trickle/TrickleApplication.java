package in.stonecolddev.trickle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableScheduling
@ConfigurationPropertiesScan
public class TrickleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrickleApplication.class, args);
	}
}