package com.zzyboot.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ZzyScheduleTask {
	@Autowired
	RestTemplate restTemplate;
	@Value("${server.port}")
	private String port;
	@Scheduled(cron = "*/5 * * * * ?")
    public void zzyCron(){
		//System.out.println("ZzyScheduleTask address is http://localhost:"+port+"/zzycron");
		//restTemplate.postForObject("http://localhost:"+port+"/zzycron", 5, String.class);
    }
}
