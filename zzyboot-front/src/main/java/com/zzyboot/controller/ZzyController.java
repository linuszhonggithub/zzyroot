package com.zzyboot.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.ZzyUserCenterService;
import com.zzyboot.util.Jiami;
import com.zzyboot.util.ZzyUtilLogin;

@RestController
public class ZzyController {
	//@Value("${authserver}")
	//private String authserver;
	@Value("${zzyprime}")
	private int zzyprime;
	@Value("${zzydhbase}")
	private int zzydhbase;
	@Autowired
	ZzyUserCenterService zzyUserCenterService;
	//@Autowired
	//RestTemplate restTemplate;
	@PostMapping("/zzylogin")
	public String zzylogin(@RequestBody String param,HttpSession httpSession){
		System.out.println("param is " + param);
		param = ZzyUtilLogin.getDecode(param, httpSession);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		
		System.out.println("post result is " +param);
		/*String[] paramArray = param.split(ZzyUtil.STRSEPITEM);
		String username = paramArray[1];
		String token = paramArray[2];*/
		//check if the username and token is valid to authentic server
		//System.out.println("post to " + authserver + "/usercheck with parameter(" + param + ")");
		String userinfo = zzyUserCenterService.usercheck(param); 
				//restTemplate.postForObject(authserver + "/usercheck", param, String.class);
		System.out.println("userinfo is " + userinfo);
		if(userinfo.indexOf(ZzyCommon.ZZYFAIL) != 0){
			userinfo = userinfo.substring(ZzyCommon.ZZYSUCCESS.length());
			String[] userinfoA = userinfo.split(ZzyCommon.STRSEPITEM);
			int index = 0;
			String url = userinfoA[index++];
			String username = userinfoA[index++];
			String token = userinfoA[index++];
			//String role = userinfoA[index++];
			
			return ZzyUtilLogin.getEncode(url + ZzyCommon.STRSEPITEM + username + ZzyCommon.STRSEPITEM + token,httpSession);	
		}
		return userinfo;
		/*if(userinfo)
		username = "linuszhong";
		token = "linuszhongtoken";
		
		String url = "http://localhost:8085/index.html";
		return ZzyUtil.getEncode(url + ZzyUtil.STRSEPITEM + username + ZzyUtil.STRSEPITEM + token,httpSession);*/
	}
	@PostMapping("/changepassword")
	public String changepassword(@RequestBody String param,HttpSession httpSession){
		System.out.println("changepassword param is " + param);
		param = ZzyUtilLogin.getDecode(param, httpSession);
		System.out.println("changepassword param new is " + param);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		System.out.println("changepassword result is " +param);
		/*changepassword result is 17cacd8c898973ede58fa2b7a7de1d4ffa59f7dd8fe32d66e30bc1ce31c4699c
		b97bb33b14b0064012d566262ecf8951e27032fec07c74fe1deb06727993c431
		b97bb33b14b0064012d566262ecf8951e27032fec07c74fe1deb06727993c431
		zy|
		Linus Zhong
		20506*/

		String result = zzyUserCenterService.changepassword(param); 
				//restTemplate.postForObject(authserver + "/usercheck", param, String.class);
		
		return result;
	}
	@PostMapping("/isuservalid")
	public String isUserValid(@RequestBody String param,HttpSession httpSession){
		param = ZzyUtilLogin.getDecode(param, httpSession);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		return checkuservalid(param);
		
	}
	public String checkuservalid(String param){
		String userinfo = zzyUserCenterService.usercheck(param); 
		if(userinfo.indexOf(ZzyCommon.ZZYFAIL) != 0){
			return ZzyCommon.ZZYSUCCESS;
		}
		return ZzyCommon.ZZYFAIL;
	}
	@PostMapping("/getsessionkey")
	public String getsessionkey(@RequestParam("param") Integer param,HttpSession httpSession){
		System.out.println("sessionkey param is " +param+",zzyprime is " + zzyprime);
		//check if the username and token is valid to authentic server
		Random random = new Random();
		int r = random.nextInt(1000);
		int rtn = ZzyUtilLogin.getMod(zzydhbase,r,zzyprime);
		System.out.println("rtn is " + rtn);
		int sessionkey = ZzyUtilLogin.getMod(param,r,zzyprime);
		System.out.println("sessionkey is " + sessionkey);
		String sessionkeySecrete = Jiami.sign(sessionkey+"","SHA-256");
		System.out.println("sessionkeySecrete is " + sessionkeySecrete);
		httpSession.setAttribute("sessionkeysecrete",sessionkeySecrete);
		return ZzyCommon.ZZYSUCCESS+rtn;
	}
	@PostMapping("/zzygettabledata")
	public String getTableData(@RequestBody String param,HttpSession httpSession){
		System.out.println("getTableData param is " + param);
		param = ZzyUtilLogin.getDecode(param, httpSession);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}

		System.out.println("getTableData param new is " + param);
		return ZzyUtilLogin.getEncode(zzyUserCenterService.gettabledata(param),httpSession);
		
	}
	@PostMapping("/zzytablesave")
	public String getTableSave(@RequestBody String param,HttpSession httpSession){
		System.out.println("getTableSavea param is " + param);
		param = ZzyUtilLogin.getDecode(param, httpSession);
		System.out.println("getTableSave param new is " + param);
		if(param.indexOf(ZzyCommon.ZZYFAIL)==0){
			return param;
		}
		int index = param.indexOf(ZzyCommon.STRSEPBLOCK);
		String p0 = param.substring(0, index);
		if(p0.equals("zzyuser")){
			String result = zzyUserCenterService.save(param);
			System.out.println("===========zzytablesave result is " + result);
			return ZzyUtilLogin.getEncode(result,httpSession);
		}
		return "";// ZzyUtilLogin.getEncode(zzyUserCenterService.gettablesave(param),httpSession);
		
	}
	
	
}
