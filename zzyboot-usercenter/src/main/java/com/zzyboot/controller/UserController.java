package com.zzyboot.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.api.service.ZzyServiceImpl;
import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.DES;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.entity.Zzyuser;
import com.zzyboot.service.ZzyMailService;
import com.zzyboot.entity.ZzyColumns;
import com.zzyboot.entity.ZzyEntityParent;
import com.zzyboot.entity.ZzyTables;

@RestController
public class UserController {
	@Autowired
	ZzyServiceImpl zzyService;
	 @Autowired
	 private Environment env;
	 @Autowired
	ZzyMailService zzyMailService;
	 @Autowired
	 private StringRedisTemplate redisTemplate;
	 
	@GetMapping("/userinfo")
	public String getUserInfo(){
		StringBuilder sb = new StringBuilder();
		List<ZzyEntityParent> users =zzyService.findAll(new Zzyuser()); 
		//System.out.println("users are " + users.toString());
		for(ZzyEntityParent z: users){
			System.out.println(z.toString());
			sb.append(z.toStringZzy() + ZzyCommon.STRSEPLINE);
		}
		return sb.toString();
	}
	@PostMapping("/save")
	public String Save(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("save param is " + param);
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String token = sessioninfoA[sessioninfoA.length - 1];
		String username = sessioninfoA[sessioninfoA.length - 2];

		String param0 = paramA[0];
		int index = param0.indexOf(ZzyCommon.STRSEPBLOCK);
		String tablename = param0.substring(0,index);
		param0 = param0.substring(index + ZzyCommon.STRSEPBLOCK.length());
		String tablenamelow = tablename.toLowerCase();
		String[] objA = param0.split(ZzyCommon.STRSEPITEM);
		String[] cols = null;
		String[] values = null;
		String password = "";
		if(objA[0].length() < 1){ //add
			if(tablenamelow.equals("zzyuser")){
				cols = new String[]{"password"};
				
				
				String newuser = objA[2];
				password = ZzyCommon.bytesToHex(newuser.getBytes());
				
				password = ZzyUtil.sha256(password);
				password = password.substring(0,8);
				String newpassword = ZzyUtil.sha256(password);
				if(objA.length == 6){
					values = new String[]{null,newpassword};	
				}else if(objA.length == 7){
					values = new String[]{newpassword};
				}
				
			}
		}
		String result = "";
		result = zzyService.save(param,cols, values);
		if(objA[0].length() < 1 && password.length() > 0 && result.indexOf(ZzyCommon.ZZYSUCCESS) >= 0){
			//email to user;
			if(tablenamelow.equals("zzyuser")){
				String newuser = objA[2];
				String to = objA[3];
				String url = objA[5];
				StringBuilder parammail= new StringBuilder();
				String from = "",cc="",bcc="";
				String subject = "hi " + newuser + ", notice from ZCloud";
				String content = "Your login is " + newuser + ", password is " + password+", please change password after you login to our website " + url + ". ";
				String file = "";
				parammail.append(from + ZzyCommon.STRSEPLINE); // from is null
				parammail.append(to + ZzyCommon.STRSEPLINE); // to
				parammail.append(cc + ZzyCommon.STRSEPLINE); // cc
				parammail.append(bcc + ZzyCommon.STRSEPLINE); // bcc
				parammail.append(subject + ZzyCommon.STRSEPLINE); // subject
				parammail.append(content + ZzyCommon.STRSEPLINE); // content
				parammail.append(file + ZzyCommon.STRSEPLINE); // content
				parammail.append(username + ZzyCommon.STRSEPITEM); // content
				parammail.append(token); // content
				zzyMailService.mail(parammail.toString());
			}
			
		}
		return result;
	}

	@PostMapping("/add")
	public String Add(@RequestBody String param){
		Zzyuser u = new Zzyuser();
		
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("add param is " + param);
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		
		
		u.setValues(param);
		String result =  zzyService.add(u,username);
		//System.out.println("add result is " + result);
		return result;
	}
	
	@PostMapping("/changepassword")
	public String Changepassword(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("changepassword usercheck param is " + param);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] paramArray = param.split(ZzyCommon.STRSEPITEM);
		int index = 0;
		/*17cacd8c898973ede58fa2b7a7de1d4ffa59f7dd8fe32d66e30bc1ce31c4699c
		b97bb33b14b0064012d566262ecf8951e27032fec07c74fe1deb06727993c431
		b97bb33b14b0064012d566262ecf8951e27032fec07c74fe1deb06727993c431
		zy|
		Linus Zhong
		20506*/
		String password = paramArray[index++];
		index++;
		String passwordnew = paramArray[index++];
		index++;
		String username = paramArray[index++];
		String token = paramArray[index++];
		if(!ZzyCommon.tokenvalid(username, token,redisTemplate)){
			System.out.println("token not exists");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
		
		Zzyuser u = (Zzyuser)zzyService.findOne(new Zzyuser(),"username" + ZzyCommon.STRSEPITEM + "password",username + ZzyCommon.STRSEPITEM +password);
		if(u == null){
			System.out.println("user, password not exists");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
		u.setPassword(passwordnew);
			
		String result =  zzyService.update(u, u.getId() + ZzyCommon.STRSEPLINE + "password" + ZzyCommon.STRSEPITEM + passwordnew,username);
		if(result.indexOf(ZzyCommon.ZZYSUCCESS) == 0){
			System.out.println("password updated");
				return ZzyCommon.ZZYSUCCESS;
		}
		return result;
	}
	@PostMapping("/delete")
	public String Delete(@RequestBody String param){
		Zzyuser u = new Zzyuser();
		
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		
		String result =  zzyService.delete(u, param,username);
		//System.out.println("delete result is " + result);
		return result;
	}
	@PostMapping("/update")
	public String Update(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		//System.out.println("add param is " + param);
		Zzyuser u = new Zzyuser();
		String result =  zzyService.update(u, param,username);
		//System.out.println("update result is " + result);
		return result;
	}
	private boolean haveright(String right, String role){
		
		if(right == null || right.length() < 1){
			return true;
		}
		if(right.equals("none")){
			return false;
		}
		if(role.equals("super")){
			return true;
		}
		if(right.equals(role)){
			return true;
		}
		return false;
	}
	@PostMapping("/gettabledata")
	public String gettabledata(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("gettabledata param is " + param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String token = sessioninfoA[sessioninfoA.length - 1];
		String username = sessioninfoA[sessioninfoA.length - 2];
		
		
		if(!ZzyCommon.tokenvalid(username, token,redisTemplate)){
			System.out.println(ZzyCommon.ZZYFAIL_USERINVALID);
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
		
		StringBuilder sb = new StringBuilder();
		long now = ZzyCommon.getNow();
		/*
		
		now #
		tablename|type:header~...|~... 
		*/
		String userrole = "admin";
		if(ZzyUtil.mapUserrole.containsKey(username)){
			userrole = ZzyUtil.mapUserrole.get(username);
		}else{
			String supers = env.getProperty("super");
			try {
				supers=DES.decryption(supers);
				String[] supersA = supers.split(ZzyCommon.STRSEPITEM);
				supers = supersA[0];
				System.out.println("supers is " + supers+", username is " +username);
				if(username.equals(supers)){
					userrole = "super";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			ZzyUtil.mapUserrole.put(username, userrole);
		}
		

		
		System.out.println("length is " + paramA.length);
		for(int i = 0; i  < paramA.length - 1 ; i++){
			System.out.println("i is " + i);
			String pi = paramA[i];
			String[] piA = pi.split(ZzyCommon.STRSEPITEM);
			String table = piA[0];
			System.out.println("table is " + table);
			String lasttime = piA[1];
			System.out.println("lasttime is " + lasttime);
			long lltime = new Long(lasttime);
			boolean tableisreadonly = false;
			boolean tablecaninsert = true;
			Boolean tablecandelete = null;
			
					ZzyEntityParent zp =ZzyUtil.getTableNewInstance(table);
					if(zp == null){
						continue;
					}
							
					
					System.out.println(zp.toStringZzy());
					String scols="";String scolsReadonly="";
					{
						List<ZzyColumns> listColumn = zp.getColumnDef();
						ZzyTables zt = zp.getTableDef();
						if(!haveright(zt.getShowright(),userrole)){
							System.out.println("no show right for " +userrole+", zt.getShowright() is " + zt.getShowright());
							continue;
						}
						if(!haveright(zt.getEditright(),userrole)){
							System.out.println("no edit right for " +userrole+", zt.getEditright() is " + zt.getEditright());
							tableisreadonly = true;
						}
						if(!haveright(zt.getInsertright(),userrole)){
							System.out.println("no insert right for " +userrole+", zt.getInsertright() is " + zt.getInsertright());
							tablecaninsert = false;
						}
						if(haveright(zt.getDeleteright(),userrole)){
							System.out.println("have delete right for " +userrole+", zt.getDeleteright() is " + zt.getDeleteright());
							tablecandelete = true;
						}
						StringBuilder sbcols = new StringBuilder();
						StringBuilder sbcolsisreadonly = new StringBuilder();
						String strColsSeg = "";
						String strColsSegReadonly = "";
						for(ZzyColumns zcol: listColumn){
							String showright = zcol.getShowright();
							System.out.println("show right0 for " + zcol.getName() + " is " + showright);
							boolean haveright = haveright(showright, userrole);
							System.out.println("show right for " + zcol.getName() + " is " + haveright);
							if(!haveright){
								continue;
							}
								sbcols.append(strColsSeg + zcol.getName());
								strColsSeg = ",";
								String editright = zcol.getEditright();
								boolean haverightEdit = true;
								if(editright.equals("readonly")){
									haverightEdit = false;
								}
								if(haverightEdit){
									haverightEdit = haveright(editright, userrole);
								}
								if(!haverightEdit){
									sbcolsisreadonly.append(strColsSegReadonly + zcol.getName());
									strColsSegReadonly = ",";
									
								}
						}
						scols =  sbcols.toString();
						scolsReadonly = sbcolsisreadonly.toString();
					}
					System.out.println("tableisreadonly is " + tableisreadonly+", tablecaninsert is " +tablecaninsert );
					sb.append(zzyService.findAll(zp,lltime,now,scols,scolsReadonly,tableisreadonly,tablecaninsert,tablecandelete,username)); 
					
			
		}
		String sResult = sb.toString();
		System.out.println("sResult is " + sResult);
		if(sResult!=null && sResult.length() > 0){
			return now +sResult;
		}
		return "";
		
		
	}

	@PostMapping("/usercheck")
	public String UserCheck(@RequestBody String param){
		try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("usercheck param is " + param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] paramArray = param.split(ZzyCommon.STRSEPITEM);
		String username = paramArray[0];
		String token = paramArray[1];
		param = username + ZzyCommon.STRSEPITEM + token;
		if(ZzyCommon.tokenvalid(username, token,redisTemplate)){
			return ZzyCommon.tokenvalidstr(username, token,redisTemplate);
		}
		Zzyuser u = null;
		String realUserName = null;
		u = (Zzyuser)zzyService.findOne(new Zzyuser(),"email",username);
		if(u != null){
			//System.out.println(u.getPassword()+" ==? " + token);
			if(u.getPassword().equals(token)){
				realUserName = u.getUsername();
			}
		}
		if(realUserName == null){
			u = (Zzyuser)zzyService.findOne(new Zzyuser(),"cell",username);
			
			if(u != null){
				if(u.getPassword().equals(token)){
					realUserName = u.getUsername();
				}
			}
		}
		if(realUserName == null){
			u = (Zzyuser)zzyService.findOne(new Zzyuser(),"username" + ZzyCommon.STRSEPITEM + "password",username + ZzyCommon.STRSEPITEM +token);
			if(u != null){
				realUserName = username;
			}
			
		}
		if(realUserName == null){
			String supers = env.getProperty("super");
			String supersu = env.getProperty("superu");
			try {
				supers=DES.decryption(supers);
				supersu=DES.decryption(supersu);
				if(param.equals(supers)){
					u = new Zzyuser();
					u.setUsername(username);
					u.setUrl(supersu);
					realUserName = u.getUsername();
					
				}else{
					return ZzyCommon.ZZYFAIL_USERINVALID;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//generate token
		Random random = new Random();
		int itoken = random.nextInt(100000);
		String url = u.getUrl();
		if(realUserName == null){
			realUserName = "";
		}
		return ZzyCommon.tokenkeep(realUserName, itoken, url,redisTemplate);
	}
}
