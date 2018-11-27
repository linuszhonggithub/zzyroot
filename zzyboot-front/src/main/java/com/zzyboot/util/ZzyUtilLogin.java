package com.zzyboot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpSession;

import com.zzyboot.common.util.DES;
import com.zzyboot.common.util.ZzyCommon;

public class ZzyUtilLogin {
	public static int getMod(int source, int exponent, int divider){
		long rtn = 1l;
		for(int i = 0; i < exponent; i++){
			rtn *= source;
			rtn = rtn % divider;
		}
		
		return (int)rtn;
	}
	public static String getSessionKeySecrete(HttpSession httpSession){
		Object rtn = httpSession.getAttribute("sessionkeysecrete");
		if(rtn == null){
			return ZzyCommon.ZZYFAIL_NoSessionKey;	
		}
		if((rtn+"").length()<1){
			return ZzyCommon.ZZYFAIL_NoSessionKey;
		}
		return rtn+"";
	}
	public static String getDecode(String param, HttpSession httpSession){
		/*final byte[] requestContent;
        requestContent = IOUtils.toByteArray(request.getReader());
        return new String(requestContent, StandardCharsets.UTF_8);*/
        //param = new String(param.getBytes(), StandardCharsets.UTF_8);
		try {
			//System.out.println("before decode param is " + param);
			param = URLDecoder.decode(param,"utf8");
			System.out.println("after decode param is " + param);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sessionkeysecrete = getSessionKeySecrete(httpSession);
		if(sessionkeysecrete.indexOf(ZzyCommon.ZZYFAIL)==0){
			return sessionkeysecrete;
		}
		String result = "";
		try {
			System.out.println("DES param is (" + param + "), sessionkeysecrete is (" + sessionkeysecrete + ")");
			result = DES.decryption(param, sessionkeysecrete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ZzyCommon.ZZYFAIL_DES_DECREPT;
		}
		return result;
	}
	public static String getEncode(String srtn, HttpSession httpSession){
		//System.out.println("ZzyUtilLogin getEncode srtn is " + srtn);
		if(srtn == null){
			srtn = "";
		}
		String sessionkeysecrete = getSessionKeySecrete(httpSession);
		if(sessionkeysecrete.indexOf(ZzyCommon.ZZYFAIL)==0){
			return sessionkeysecrete;
		}
		if(srtn.indexOf(ZzyCommon.ZZYFAIL) == 0){
			return srtn;
		}
		String result = "";
		if(srtn.indexOf(ZzyCommon.ZZYSUCCESS) == 0){
			srtn = srtn.substring(ZzyCommon.ZZYSUCCESS.length());
		}
		try {
			result = DES.encryption(srtn,sessionkeysecrete);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ZzyCommon.ZZYFAIL_DES_ENCREPT;
		}
		
		return ZzyCommon.ZZYSUCCESS + result;
	}
}
