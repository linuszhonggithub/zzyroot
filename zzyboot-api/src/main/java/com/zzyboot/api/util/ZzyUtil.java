package com.zzyboot.api.util;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.hash.Hashing;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.entity.ZzyColumns;
import com.zzyboot.entity.ZzyEntityParent;
import com.zzyboot.entity.ZzyTables;

public class ZzyUtil {
	public final static long ZZYTIMEOUTTABLEDATA=3600000l; //1 hour
	public static Map<String, List<ZzyColumns>> tableColumns = new ConcurrentHashMap<String, List<ZzyColumns>>();
	public static Map<String, ZzyTables> tableTables = new ConcurrentHashMap<String, ZzyTables>();
	public static Map<String, Map<String,Set<String>>> zzyUserCols = new ConcurrentHashMap<String, Map<String,Set<String>>>();
	public static Map<String, String> mapUserrole = new ConcurrentHashMap<String, String>();

	public static long getNow(){
		return ZzyCommon.getNow();
	}
	public static String getTableClassNameFull(String table){
		  return ZzyCommon.ZzyClassPathEntity+"."+getTableClassName(table);	
		}
	public static String getTableClassName(String table){
	  return table.substring(0,1).toUpperCase() + table.substring(1);	
	}
	public static ZzyEntityParent getTableNewInstance(String table){
			Class zClass;
			try {
				zClass = Class.forName(getTableClassNameFull(table));
				ZzyEntityParent zp;
				try {
					zp = (ZzyEntityParent)zClass.newInstance();
					return zp;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	public static Date getDate(String aDate){
		String[] adateA=aDate.split("\r\n");
	       aDate=adateA[0].trim();

	    	if(aDate==null || aDate.length()<1)return null;
	    	if(aDate.toLowerCase().equals("null"))return null;
	    	aDate=aDate.trim();
	    	if(aDate.indexOf(" ")>0){
	    		String[] dA=aDate.split(" ");
	    		if(dA.length==3){
	    			String month="01";
	    			if(dA[0].toLowerCase().equals("jan"))month="01";
	    			else if(dA[0].toLowerCase().equals("feb"))month="02";
	    			else if(dA[0].toLowerCase().equals("mar"))month="03";
	    			else if(dA[0].toLowerCase().equals("apr"))month="04";
	    			else if(dA[0].toLowerCase().equals("may"))month="05";
	    			else if(dA[0].toLowerCase().equals("jun"))month="06";
	    			else if(dA[0].toLowerCase().equals("jul"))month="07";
	    			else if(dA[0].toLowerCase().equals("aug"))month="08";
	    			else if(dA[0].toLowerCase().equals("sep"))month="09";
	    			else if(dA[0].toLowerCase().equals("oct"))month="10";
	    			else if(dA[0].toLowerCase().equals("nov"))month="11";
	    			else if(dA[0].toLowerCase().equals("dec"))month="12";
	    			String year=dA[2];
	    			if(year.length()<3)year="20"+year;
	    			String day=dA[1];
	    			aDate=year+"-"+month+"-"+day;
	    			
	    		}
	    	}
	    	
	    	if(aDate.length()<1)return null;
	        Timestamp dRtn=getDatetime(aDate);
	        if(dRtn==null)return null;
	    	return new Date(dRtn.getTime());
	}
	public static Timestamp getDatetime(String aDate){
	  	if(aDate==null || aDate.length()<1)return  null;
	  	if(aDate.toLowerCase().equals("null"))return null;
	  	
	  	if(aDate.indexOf(" ")<1){
	  		//systemout("aDate is "+aDate);
	  		if(aDate.length()==8)aDate="20"+aDate;
	  	   Date aD=Date.valueOf(aDate);
	  	   return new Timestamp(aD.getTime());
	  	   
	  	}
	  	if(aDate.indexOf("-")<1)return null;
	  	return Timestamp.valueOf(aDate);
  }
	public static int getMod(int source, int exponent, int divider){
		long rtn = 1l;
		for(int i = 0; i < exponent; i++){
			rtn *= source;
			rtn = rtn % divider;
		}
		
		return (int)rtn;
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
	public static String sha256(String s){
		String result = sha256hex(s);
		return sha256hex(result); 
	}
	private static String sha256hex(String s){
		String result = Hashing.sha256()
				  .hashString(s, StandardCharsets.UTF_8)
				  .toString();
		return result;
	}
}
