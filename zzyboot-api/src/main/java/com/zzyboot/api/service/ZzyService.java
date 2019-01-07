package com.zzyboot.api.service;

import java.util.List;

import com.zzyboot.entity.ZzyEntityParent;
import com.zzyboot.pojo.ZzyTableResult;

public interface ZzyService {
	public String add(ZzyEntityParent z, String entryid);
	public String delete(ZzyEntityParent z, String uniqCol, String entryid);
	public List<ZzyEntityParent> findAll(ZzyEntityParent z);
	public ZzyTableResult findAll(ZzyTableResult zt,ZzyEntityParent z, long begin, long end, String scols, String scolsReadonly,Boolean tableisreadonly,Boolean tablecaninsert,Boolean tablecandelete,String username, Boolean issuper);
	public String update(ZzyEntityParent z, String updateinfo, String entryid);
	public ZzyEntityParent findOne(ZzyEntityParent z, String cols, String wheres);
	public String save(String param);
}
