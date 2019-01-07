package com.zzyboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean {
	@Bean
	public RestTemplate getRestTemplate(){
		//System.out.println("-----------------------get resttemp");
		return new RestTemplate();
	}
}
