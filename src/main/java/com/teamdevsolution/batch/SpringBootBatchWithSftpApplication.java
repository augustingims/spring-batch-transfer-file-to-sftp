package com.teamdevsolution.batch;

import com.teamdevsolution.batch.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class SpringBootBatchWithSftpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBatchWithSftpApplication.class, args);
	}

}
