package com.zzyboot.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.redis.core.StringRedisTemplate;


public class ZzyCommon {
	
	public final static String STRSEPITEM="zy~";
	public final static String STRSEPLINE="zy!";
	public final static String STRSEPBLOCK="zy#";
	public final static String STREMAIL="zy:";
	public final static String STRTYPEHEADER="type:header";
	public final static String ZzyClassPath="com.zzyboot";
	public final static String ZzyLogSuffix="zzylog";
	public final static String ZzyClassPathEntity=ZzyClassPath+".entity";
	public final static String ZZYSUCCESS = "Success:";
	public final static String ZZYSUCCESSDELETE = ZZYSUCCESS + "deleted successfully";
	public final static String ZZYSUCCESSINSERT = ZZYSUCCESS + "insert successfully";
	public final static String ZZYSUCCESSUPDATE = ZZYSUCCESS + "update successfully";
	public final static String ZZYSUCCESSChangePassword = ZZYSUCCESS + "password changed successfully";
	public final static String ZZYFAIL = "Fail:";
	public final static Integer ENTRYID = -1;
	public final static String ZZYFAIL_CANOTACCESS = ZZYFAIL + "can not access to ";
	public final static String ZZYFAILADD = ZZYFAIL+"This is not new record";
	public final static String ZZYFAILADDFAIL = ZZYFAIL+"insert failed";
	public final static String ZZYFAILADDDUP = ZZYFAIL+"data duplicate conflict";
	public final static String ZZYFAILADDINCOMPLETE = ZZYFAIL+"data is not complete";
	public final static String ZZYFAILDELETENOTEXISTS = ZZYFAIL+"data is not exists";
	public final static String ZZYFAILUPDATENODATA = ZZYFAIL+"data is not exists";
	public final static String ZZYFAIL_NoSessionKey = ZZYFAIL + "no session key";
	public final static String ZZYFAIL_DES_DECREPT = ZZYFAIL + "DES decryption failed";
	public final static String ZZYFAIL_DES_ENCREPT = ZZYFAIL + "DES encryption failed";
	public final static String ZZYFAIL_USERINVALID = ZZYFAIL + "invalid login info";
	public final static String ZZYLOG_ADD="n";
	public final static String ZZYFAIL_EMAIL_NOTO = ZZYFAIL + "there is no To";
	public final static String ZZYFAIL_EMAIL_WRONGFROM = ZZYFAIL + "invalid From";
	public final static String ZZYFAIL_EMAIL_WRONGTO = ZZYFAIL + "invalid To";
	
	

	
	
	public static Map<String, String> usertoken = new ConcurrentHashMap<String, String>();

	public static boolean tokenvalid(String username, String token,StringRedisTemplate redisTemplate){
		String param = username + STRSEPITEM + token;
		if(usertoken.containsKey(param)){
			return true;
		}
		String result = redisGet(redisTemplate,param);
		if(result == null || result.length() < 1){
			return false;
		}
		return true;
	}
	public static String tokenvalidstr(String username, String token,StringRedisTemplate redisTemplate){
		String param = username + STRSEPITEM + token;
		if(usertoken.containsKey(param)){
			return ZZYSUCCESS + usertoken.get(param);
		}
		String result = redisGet(redisTemplate,param);
		if(result == null || result.length() < 1){
			return ZZYFAIL_USERINVALID;
		}
		return ZZYSUCCESS + result;
	}
	public static String tokenkeep(String realUserName, int token, String url,StringRedisTemplate redisTemplate){
		String param = realUserName + STRSEPITEM + token;
		
		usertoken.put(param, url + STRSEPITEM + param);
		redisSet(redisTemplate,param, url + STRSEPITEM + param);
		return ZZYSUCCESS + url + STRSEPITEM + param;
	}
	public static String redisGet(StringRedisTemplate redisTemplate, String key){
		return redisTemplate.opsForValue().get(key);
	}
	public static void redisSet(StringRedisTemplate redisTemplate, String key, String value){
		redisTemplate.opsForValue().set(key, value);
	}
	public static String ENTITYPATH = "com.zzycommon.entity.";
	public static long getNow(){
		return System.currentTimeMillis();
	}
	public static boolean isEmail(String email){
		if(email == null || email.length()<1){
			return false;
		}
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher mat = pattern.matcher(email);
		return mat.matches();
	}
	public static List<String> getUserToken(String[] paramArray){
		List<String> result = new ArrayList<String>();
		boolean isbegin = false;
		for(int i = 0; i < paramArray.length; i++){
			String pi = paramArray[i];
			if(isbegin){
				result.add(pi);
			}
			if(pi.equals(ZzyCommon.STRSEPLINE)){
				isbegin = true;
			}
		}
		return result;
	}
	public static <T> T[] joinArrayGeneric(T[]... arrays){
		int length = 0;
		for(T[] array: arrays){
			length += array.length;
		}
		
		T[] result = null;
		result = (T[])Array.newInstance(arrays[0].getClass().getComponentType(), length);
		
		int offset = 0;
		for(T[] array: arrays){
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	public static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
