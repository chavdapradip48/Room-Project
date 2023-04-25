
package com.pradip.roommanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
public class RoomManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomManagementSystemApplication.class, args);
	}

}
