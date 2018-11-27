package com.zzyboot.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Jiami {
	public static String sign(String str, String type){
		String s = Encrypt(str, type);
		return s;
	}
	public static String Encrypt(String strSrc, String algorithm){
		MessageDigest md = null;
		String strDes = "";
		 try {
			md = MessageDigest.getInstance(algorithm);
				md.update(strSrc.getBytes("UTF-8"));
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		 return strDes;
	}
	
	public static String bytes2Hex(byte[] bytes){
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i=0;i<bytes.length;i++){
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length()==1){
				//1得到一位的进行补0操作
				stringBuffer.append("0");
			} stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
