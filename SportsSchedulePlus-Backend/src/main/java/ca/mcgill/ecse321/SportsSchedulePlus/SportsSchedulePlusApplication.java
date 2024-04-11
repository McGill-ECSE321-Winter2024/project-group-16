package ca.mcgill.ecse321.SportsSchedulePlus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ca.mcgill.ecse321.SportsSchedulePlus")
public class SportsSchedulePlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsSchedulePlusApplication.class, args);
	}

}
