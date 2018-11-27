package com.zzyboot.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.MainService;
@RestController
public class MainController {
	@Autowired
	 private StringRedisTemplate redisTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${zzymsgexchange}")
	String zzymsgExchange;
	@Value("${zzymsgkey}")
	String zzymsgKey;
	
	@PostMapping("/mail")
	public String mail(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("mail param is " + param);
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		//System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		//System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		String token = sessioninfoA[sessioninfoA.length - 1];
		if(!ZzyCommon.tokenvalid(username, token,redisTemplate)){
			//System.out.println("token not exists");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
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
