package com.zzyboot.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.MainService;

@Component
public class MessageConsumer {
	@Autowired
	MainService mainService;

	@RabbitListener(queues = "zzyboot-mail-queue")
	public void handler(String param){
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		int index = 0;
		String from = paramA[index++];
		String to = paramA[index++];
		String cc = paramA[index++];
		String bcc = paramA[index++];
		String subject = paramA[index++];
		String content = paramA[index++];
		String filename = paramA[index++];
		
		mainService.sendAttachmentsMail(from, to, cc, bcc, subject, content, filename);
	}
}
