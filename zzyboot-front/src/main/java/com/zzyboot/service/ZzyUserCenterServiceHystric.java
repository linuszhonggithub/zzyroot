package com.zzyboot.service;

import org.springframework.stereotype.Component;

import com.zzyboot.common.util.ZzyCommon;

@Component
public class ZzyUserCenterServiceHystric implements ZzyUserCenterService {

	@Override
	public String usercheck(String param) {
		
		return ZzyCommon.ZZYFAIL_CANOTACCESS+"ZzyUserCenter";
	}

	@Override
	public String changepassword(String param) {
		return ZzyCommon.ZZYFAIL_CANOTACCESS +"ZzyUserCenter";
	}

	@Override
	public String gettabledata(String param) {
		return ZzyCommon.ZZYFAIL_CANOTACCESS +"ZzyUserCenter";
	}

	@Override
	public String save(String param) {
		// TODO Auto-generated method stub
		return ZzyCommon.ZZYFAIL_CANOTACCESS +"ZzyUserCenter";
	}

}
