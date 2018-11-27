package com.zzyboot.api.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.zzyboot.api.repository.ZzyRepository;
import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.entity.ZzyEntityParent;

@Service
public class ZzyServiceImpl implements ZzyService {
	@Autowired
	ZzyRepository repository;
	@Autowired
	 private StringRedisTemplate redisTemplate;

	public String add(ZzyEntityParent z, String entryid){
		return repository.add(z,entryid);
		
	}
	public String deleteAll(ZzyEntityParent z, String id, String entryid){
		return repository.deleteAll(z,id,entryid);
		
	}

	@Override
	public List<ZzyEntityParent> findAll(ZzyEntityParent z) {
		// TODO Auto-generated method stub
		return repository.findAll(z);
	}
	@Override
	public String findAll(ZzyEntityParent z, long begin, long end,String scols, String scolsReadonly,Boolean tableisreadonly,Boolean tablecaninsert,Boolean tablecandelete,String username) {
		// TODO Auto-generated method stub
		return repository.findAll(z,begin,end,scols,scolsReadonly,tableisreadonly,tablecaninsert,tablecandelete,username);
	}

	@Override
	public String delete(ZzyEntityParent z, String uniqCol, String entryid) {
		return repository.delete(z,uniqCol,entryid);
	}

	@Override
	public String update(ZzyEntityParent z, String updateinfo, String entryid) {
		// TODO Auto-generated method stub
		return repository.update(z,updateinfo,entryid);
	}

	public ZzyEntityParent findOne(ZzyEntityParent z, String cols, String wheres) {
		// TODO Auto-generated method stub
		return repository.findOne(z,cols,wheres);
	}
	public String save(String param){
		return save(param, null, null);
	}
	public String save(String param,String[] cols, String[] values){
		try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("save param is " + param);
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
		if(!ZzyUtil.zzyUserCols.containsKey(username)){
			System.out.println("user " + username + " have no table columns");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
		param=param.substring(0,param.lastIndexOf(ZzyCommon.STRSEPLINE));
		//param = paramA[0];
		int index = param.indexOf(ZzyCommon.STRSEPBLOCK);
		String tablename = param.substring(0,index);
		param = param.substring(index + ZzyCommon.STRSEPBLOCK.length());
		String tablenamelow = tablename.toLowerCase();
		if(!ZzyUtil.zzyUserCols.get(username).containsKey(tablenamelow)){
			System.out.println("user " + username + " have not set table columns for " + tablenamelow);
			return ZzyCommon.ZZYFAIL_USERINVALID;
			
		}
		Set<String> strColsOld = ZzyUtil.zzyUserCols.get(username).get(tablenamelow);
		ZzyEntityParent z =ZzyUtil.getTableNewInstance(tablename);
		if(z == null){
			System.out.println("ZzyService save: table " + tablename +" new instance failed");
			return ZzyCommon.ZZYFAIL_USERINVALID;
		}
		String[] objA = param.split(ZzyCommon.STRSEPITEM);
		Set<String> strCols;
		if(cols != null){
			strCols = new HashSet<String>(strColsOld);
			for(int i = 0;i < cols.length; i++){
				strCols.add(cols[i]);
			}
		}else{
			strCols = strColsOld;
		}
		if(values != null){
			objA = ZzyCommon.joinArrayGeneric(objA, values);
		}
		if(objA[0].length() < 1){ //add
			ZzyEntityParent znew = z.newObj(z, objA,z.getColumnDef(),strCols);
			
			return add(znew, username);
		}else if(objA.length < 2 && paramA.length < 3){ //delete
			return deleteAll(z, objA[0],username);
		}else{ //update  save param is zzyuserzy#500zy~cellzy:linus8cell8zy~zy!zy~linuszhongzy~56341
			/*String id = objA[0];
			ZzyEntityParent zupdate = repository.findByID(z,id);
			Set<String> strColsUpdate = new HashSet<String>();
			strColsUpdate.add("id");
			for(int i = 1; i < objA.length; i++){
				String oi = objA[i];
				String[] oiA = oi.split(ZzyCommon.STREMAIL);
				strColsUpdate.add(oiA[0]);
				objA[i] = oiA[1];
			}
			ZzyEntityParent znew = zupdate.newObj(zupdate, objA,z.getColumnDef(),strCols);*/
			return update(z,param,username);
		}
	}
	
}
