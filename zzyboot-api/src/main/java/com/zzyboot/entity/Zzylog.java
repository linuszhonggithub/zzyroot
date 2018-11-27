package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public class Zzylog extends ZzyEntityParent implements Serializable {
	public Zzylog(){}
	public Zzylog(String action, Long lineid, String colname, String colvalue,
			String colvalueold) {
		super();
		this.action = action;
		this.lineid = lineid;
		this.colname = colname;
		this.colvalue = colvalue;
		this.colvalueold = colvalueold;
	}
	private static final long serialVersionUID = 3738999143816096759L;
	@Column(nullable = false,length = 10)
	@ZzyColumn(seq=240)
	private String action;

	@Column(nullable = false)
	@ZzyColumn(seq=235)
	private Long lineid;

	@Column(nullable = false,length = 50)
	@ZzyColumn(seq=230)
	private String colname;

	@Column(nullable = false)
	@ZzyColumn(seq=220)
	private String colvalue;
	@Column(nullable = false)
	@ZzyColumn(seq=210)
	private String colvalueold;
	
}
