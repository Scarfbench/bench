package com.ibm.websphere.samples.daytrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ibm.websphere.samples.daytrader.support.ApplicationProps;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProps.class)
public class DaytraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaytraderApplication.class, args);
	}

}
