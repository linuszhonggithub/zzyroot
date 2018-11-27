package com.zzyboot.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZzyUtilUserCenter {
	public static Map<String, String> usertoken = new ConcurrentHashMap<String, String>();
	public static Map<String, String> mapUserrole = new ConcurrentHashMap<String, String>();
}
