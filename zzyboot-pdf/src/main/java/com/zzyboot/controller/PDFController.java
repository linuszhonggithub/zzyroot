package com.zzyboot.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.PdfService;

@RestController
public class PDFController {
	@Autowired
	 private StringRedisTemplate redisTemplate;

	@Autowired
	PdfService pdfService;
	@Value("${filepath}")
	private String filepath;
	
	@PostMapping("/getpdf")
	public String getpdf(@RequestBody String param){
		
		try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("getpdf param is " + param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		String token = sessioninfoA[sessioninfoA.length - 1];
		String username = sessioninfoA[sessioninfoA.length - 2];
		
		if(!ZzyCommon.tokenvalid(username, token,redisTemplate)){
			//System.out.println("token not exists");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}

		
		String rpttype = "";
		String[] header = null;
		String[][] body = null;
		String[] width = null;
		for(int i = 0; i < paramA.length; i++){
			String pi = paramA[i];
			String[] piA = pi.split(ZzyCommon.STRSEPLINE);
			for(int j = 0; j < piA.length; j++){
				String pj = piA[j];
				int stremaillength = ZzyCommon.STREMAIL.length();
				if(pj.indexOf(ZzyCommon.STREMAIL) > 0){
					String pj0 = pj.substring(0,pj.indexOf(ZzyCommon.STREMAIL));
					String pj1 = pj.substring(pj.indexOf(ZzyCommon.STREMAIL) + stremaillength);
					if(pj0.equals("rpttype")){
						rpttype = pj1;
					}else if(pj0.equals("header")){
						header = pj1.split(ZzyCommon.STRSEPITEM);
					}else if(pj0.equals("width")){
						width = pj1.split(ZzyCommon.STRSEPITEM);
					}else if(pj0.equals("body")){
						//pj1 = pj1.substring(pj1.indexOf(ZzyCommon.STREMAIL) + stremaillength);
						String[] pj1A = pj1.split(ZzyCommon.STRSEPITEM);
						body = new String[pj1A.length][header.length];
						for(int k = 0; k < pj1A.length; k++){
							String pk = pj1A[k];
							body[k] = pk.split(ZzyCommon.STREMAIL);
						}
					}
				}
			}
		}
		String filename = filepath+"/" + rpttype + "/" + username + "/" + rpttype +"_" + username + ".pdf"; 
		return pdfService.getPdf( filename,  rpttype,  header,  body, width,username);
	}
}
