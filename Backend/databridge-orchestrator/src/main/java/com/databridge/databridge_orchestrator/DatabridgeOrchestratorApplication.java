package com.databridge.databridge_orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DatabridgeOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabridgeOrchestratorApplication.class, args);
	}

}
