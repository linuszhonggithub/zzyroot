package com.zzyboot.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.zzyboot.common.util.ZzyCommon;

@Component
public class MainService {
	@Autowired
	private JavaMailSender mailSender;
	@Value("${mailfrom}")
    private String mailfrom;
	
	public void sendSimpleMail(String from,String to, String subject, String content){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
		
	}
	
	public void sendHtmlMail(String from, String to, String subject, String content){
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String sendAttachmentsMail(String from, String to,String cc, String bcc, String subject, String content, String filePath){
		if(from == null || from.length() < 1){
			from = mailfrom;
		}
		if(to == null || to.length() < 1){
			return ZzyCommon.ZZYFAIL_EMAIL_NOTO;
		}
		if(!ZzyCommon.isEmail(from)){
			return ZzyCommon.ZZYFAIL_EMAIL_WRONGFROM;
		}
		String[] toA = to.split(ZzyCommon.STREMAIL);
		boolean hasTo = false;
		for(int i = 0; i < toA.length; i++){
			if(toA[i].length() < 1){
				continue;
			}
			if(!ZzyCommon.isEmail(toA[i])){
				return ZzyCommon.ZZYFAIL_EMAIL_WRONGTO+" " + toA[i];
			}
			hasTo = true;
		}
		if(!hasTo){
			return ZzyCommon.ZZYFAIL_EMAIL_NOTO;
		}
		
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setFrom(from);
			//System.out.println("mail send 0");
			helper.setTo(toA);
			if(cc != null && cc.length() > 0){
				helper.setCc(cc.split(ZzyCommon.STREMAIL));
			}
			//System.out.println("mail send 1");
			String[] bccA;
			if(bcc == null || bcc.length() < 1){
				bccA = new String[]{mailfrom};
			}else{
				bcc+=ZzyCommon.STREMAIL+mailfrom;
				bccA = bcc.split(ZzyCommon.STREMAIL);
			}helper.setBcc(bccA);
			//System.out.println("mail send 2");
			helper.setSubject(subject);
			//System.out.println("mail send 3");
			helper.setText(content,true);
			//System.out.println("mail send 4");
			if(filePath!=null && filePath.length() > 0){
				FileSystemResource file = new FileSystemResource(new File(filePath));
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
				helper.addAttachment(fileName, file);
			}
			//System.out.println("mail send 5");
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ZzyCommon.ZZYSUCCESS;
	}
}
