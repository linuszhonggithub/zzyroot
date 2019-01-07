package com.zzyboot.controller;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.common.util.ZzyCommon;
@RestController
public class MainController {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${zzymsgexchange}")
	String zzymsgExchange;
	@Value("${zzymsgkey}")
	String zzymsgKey;
	
	@PostMapping("/mail")
	public String mail(@RequestBody String param){
		
		rabbitTemplate.convertAndSend(zzymsgExchange, zzymsgKey, param);
		return ZzyCommon.ZZYSUCCESS;
		
		/*int index = 0;
		String from = paramA[index++];
		String to = paramA[index++];
		String cc = paramA[index++];
		String bcc = paramA[index++];
		String subject = paramA[index++];
		String content = paramA[index++];
		String filename = paramA[index++];
		
		return mainService.sendAttachmentsMail(from, to, cc, bcc, subject, content, filename);*/
		
	}

}
