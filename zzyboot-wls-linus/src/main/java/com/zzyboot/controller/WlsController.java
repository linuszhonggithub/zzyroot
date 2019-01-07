package com.zzyboot.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.api.service.ZzyServiceImpl;
import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.DES;
import com.zzyboot.common.util.Jiami;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.ZzyMailService;
import com.zzyboot.service.ZzyPdfService;

@RestController
public class WlsController {
	/*@Value("${zzyprime}")
	private int zzyprime;
	@Value("${zzydhbase}")
	private int zzydhbase;
	@Autowired
	ZzyPdfService zzyPdfService;
	@Autowired
	 private StringRedisTemplate redisTemplate;
	@Autowired
	ZzyServiceImpl zzyService;
	 @Autowired
	ZzyMailService zzyMailService;
	 
	@PostMapping("/isuservalid")
	public String isUserValid(@RequestBody String param,HttpSession httpSession){

		try {
			//System.out.println("before decode param is " + param);
			param = URLDecoder.decode(param,"utf8");
			System.out.println("after decode param is " + param);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sessionkeysecrete = "";
		Object rtn = httpSession.getAttribute("sessionkeysecrete");
		if(rtn == null){
			sessionkeysecrete = ZzyCommon.ZZYFAIL_NoSessionKey;	
		}else if((rtn+"").length()<1){
			sessionkeysecrete = ZzyCommon.ZZYFAIL_NoSessionKey;
		}else{
			sessionkeysecrete = rtn+"";
		}
		
		if(sessionkeysecrete.indexOf(ZzyCommon.ZZYFAIL)==0){
			param =  sessionkeysecrete;
		}
		String result = "";
		try {
			System.out.println("DES param is (" + param + "), sessionkeysecrete is (" + sessionkeysecrete + ")");
			result = DES.decryption(param, sessionkeysecrete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			param= ZzyCommon.ZZYFAIL_DES_DECREPT;
		}
		param =  result;
		
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		String[] paramArray = param.split(ZzyCommon.STRSEPITEM);
		String username = paramArray[0];
		String token = paramArray[1];
		param = username + ZzyCommon.STRSEPITEM + token;
		if(ZzyCommon.tokenvalid(username, token,redisTemplate)){
			return ZzyCommon.ZZYSUCCESS;
		}else{
			return ZzyCommon.ZZYFAIL;	
		}
		
//		return ZzyCommon.checkuservalid(param,httpSession,redisTemplate);
		
	}
	
	@PostMapping("/getsessionkey")
	public String getsessionkey(@RequestParam("param") Integer param,HttpSession httpSession){
		//String sessionkey = ZzyCommon.getsessionkey(param,httpSession,zzydhbase, zzyprime);
		Random random = new Random();
		int r = random.nextInt(1000);
		int rtn = ZzyCommon.getMod(zzydhbase,r,zzyprime);
		int sessionkey = ZzyCommon.getMod(param,r,zzyprime);
		String sessionkeySecrete = Jiami.sign(sessionkey+"","SHA-256");
		httpSession.setAttribute("sessionkeysecrete",sessionkeySecrete);
		return ZzyCommon.ZZYSUCCESS+rtn;
		
		//System.out.println("getsessionkey is " + sessionkey);
		//return sessionkey;
	}
	@PostMapping("/zzygettabledata")
	public String getTableData(@RequestBody String param,HttpSession httpSession){
		return ZzyUtil.gettabledata(param,redisTemplate,zzyService,null);
	}
	@PostMapping("/zzygetpdf")
	public String getPdf(@RequestBody String param,HttpSession httpSession, HttpServletResponse response){
		System.out.println("getPdf param is " + param);
		param = ZzyCommon.getDecode(param, httpSession);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}

		
		return ZzyCommon.getEncode(zzyPdfService.getpdf(param),httpSession);
	}
	@PostMapping("/zzytablesave")
	public String getTableSave(@RequestBody String param,HttpSession httpSession){
		System.out.println("getTableSavea param is " + param);
		param = ZzyCommon.getDecode(param, httpSession);
		System.out.println("getTableSave param new is " + param);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		int index = param.indexOf(ZzyCommon.STRSEPBLOCK);
		String p0 = param.substring(0, index);
		if(p0.equals("zzyuser")){
			//String result = zzyUserCenterService.save(param);
			//System.out.println("===========zzytablesave result is " + result);
			//return ZzyCommon.getEncode(result,httpSession);
		}
		return "";// ZzyUtilLogin.getEncode(zzyUserCenterService.gettablesave(param),httpSession);
		
	}*/
}
