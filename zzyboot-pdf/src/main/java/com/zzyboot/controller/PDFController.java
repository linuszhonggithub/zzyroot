package com.zzyboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.pojo.ZzyParam;
import com.zzyboot.pojo.ZzyResult;
import com.zzyboot.service.PdfService;

@RestController
public class PDFController {
	
	@Autowired
	PdfService pdfService;
	@Value("${zzyfilepath}")
	private String filepath;
	@Value("${zzyurl}")
	private String zzyurl;
	
	@PostMapping("/getpdf")
	public ZzyResult getpdf(@RequestBody String param){
		
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		String[] paramA = zp.getData().split(ZzyCommon.STRSEPLINE);
		
		String rpttype = "";
		String lan = "";
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
					}else if(pj0.equals("lan")){
						lan = pj1;
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
		String username = zp.getUsername();
		String filename = rpttype + "/" + username + "/" + rpttype +"_" + username + ".pdf"; 
		//filename = rpttype +"_" + username + ".pdf";
		
		pdfService.getPdf( filepath + filename,  rpttype,  header,  body, width,username, lan);
		
		ZzyResult zr = new ZzyResult();
		zr.setFlag(ZzyCommon.ZZYSUCCESS);
		zr.setResultmsg(zzyurl + '/' + filename);
		return zr;
		/*File file = new File(filename);
		if(file.exists()){
			 byte[] data = null;
			 FileInputStream input = null;
			 try {
				 input = new FileInputStream(file);
				 
				 data = new byte[input.available()]; input.read(data);
				 
				 String encodeBase64 = Base64.encodeBase64String(data);
				 //response.getOutputStream().write(data);
				
				 return encodeBase64;
				 //return ZzyCommon.ZZYSUCCESS;
			//System.out.println("encodeBase64 is " + encodeBase64);
				// response.getOutputStream().write(encodeBase64.getBytes()); 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(input != null)
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			 
			 
		}*/
		//return ZzyCommon.ZZYFAIL_PDF;
	}
	
}
