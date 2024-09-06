package com.example;

import com.example.monitor.DirectorWatchService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;


@SpringBootApplication
public class FileUploadMultithreadingApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(FileUploadMultithreadingApplication.class, args);
	}
}
