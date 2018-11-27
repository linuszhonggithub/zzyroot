package com.zzyboot.entity;

import java.lang.reflect.Field;

import com.zzyboot.common.util.ZzyCommon;

import lombok.Data;

@Data
public class ZzyTables {
	private String showright;
	private String editright;
	private String insertright;
	private String label;
	private String deleteright;
	public ZzyTables(String showright, String editright, String insertright, String label, String deleteright) {
		this.showright = showright;
		this.editright = editright;
		this.insertright = insertright;
		this.label = label;
		this.deleteright = deleteright;
	}
	public String toStringZzy(Boolean canInsert, Boolean isreadonly, Boolean canDelete){
		StringBuilder sb = new StringBuilder();
		sb.append("caninsert:"+canInsert);
		boolean canEdit = !isreadonly;
		sb.append(ZzyCommon.STRSEPITEM + "canedit:"+canEdit);
		if(canDelete == null){canDelete = canEdit;}
		sb.append(ZzyCommon.STRSEPITEM + "candelete:"+canDelete);
		sb.append(ZzyCommon.STRSEPITEM + "label:"+label);
		return sb.toString();
	}
}
