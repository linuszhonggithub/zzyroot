package com.zzyboot.entity;

import java.lang.reflect.Field;

import com.zzyboot.common.util.ZzyCommon;

import lombok.Data;

@Data
public class ZzyColumns {
	private String name;
	private String label;
	private String fieldtype;
	private Boolean isPrimary;
	private Boolean isKeyword;
	private Boolean isUnique;
	private Boolean isRequired;
	private Boolean isFrozen;
	private Integer minLength;
	private Integer seq;
	private String showright;
	private String editright;
	private String droplist;
	private Field fi;
	public ZzyColumns(String name, String label, String fieldtype,Boolean isPrimary, Boolean isKeyword, Boolean isUnique,Boolean isRequired, Boolean isFrozen, Integer minLength,Integer seq,String droplist, Field f) {
		super();
		this.name = name;
		this.label = label;
		this.fieldtype = fieldtype;
		if(label == null || label.length() < 1){
			this.label = name.substring(0,1).toUpperCase() + name.substring(1);	
		}
		this.isPrimary = isPrimary;
		this.isKeyword = isKeyword;
		this.isUnique = isUnique;
		this.isRequired = isRequired;
		this.isFrozen = isFrozen;
		this.minLength = minLength;
		this.seq = seq;
		this.droplist = droplist;
		this.fi = f;
	}
	public String toStringZzy(){
		return toStringZzy(false);
	}
	public String toStringZzy(Boolean isreadonly){
		//System.out.println("--------------------------ZzyColumns toStringZzy");
		return toStringZzy(isreadonly,false);
	}
	public String toStringZzy(Boolean isreadonly,Boolean issuper){
		//System.out.println("--------------------------ZzyColumns toStringZzy, is super is " + issuper);
		StringBuilder sb = new StringBuilder();
		sb.append("name:"+this.getName());
		if(this.label == null || this.label.length() < 1){
			this.label = this.getName().substring(0,1).toUpperCase() + this.getName().substring(1);	
		}
		if(this.fieldtype == null || this.fieldtype.length() < 1){
			this.fieldtype = "text";	
		}
		
		sb.append(ZzyCommon.STRSEPITEM + "label:"+this.getLabel());
		String fieldtype = this.getFieldtype();
		Boolean isrequired = this.getIsRequired();
		if(issuper && fieldtype.equals("hidden")){
			fieldtype = "int";
			isreadonly = true;
			isrequired = false;
			System.out.println("ZzyColumns hidden fieldtype is " + fieldtype);
		}
		
		
		sb.append(ZzyCommon.STRSEPITEM + "fieldtype:"+fieldtype);
		sb.append(ZzyCommon.STRSEPITEM + "isprimary:"+this.getIsPrimary());
		sb.append(ZzyCommon.STRSEPITEM + "iskeyword:"+this.getIsKeyword());
		sb.append(ZzyCommon.STRSEPITEM + "isunique:"+this.getIsUnique());
		sb.append(ZzyCommon.STRSEPITEM + "isrequired:"+isrequired);
		sb.append(ZzyCommon.STRSEPITEM + "isfrozen:"+this.getIsFrozen());
		sb.append(ZzyCommon.STRSEPITEM + "minlength:"+this.getMinLength());
		sb.append(ZzyCommon.STRSEPITEM + "isreadonly:"+isreadonly);
		sb.append(ZzyCommon.STRSEPITEM + "droplist:"+this.getDroplist());

		return sb.toString();
	}
}
