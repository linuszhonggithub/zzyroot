package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public class Zzynote extends ZzyEntityParent implements Serializable {
	public Zzynote(){}
	public Zzynote(Long lineid, String colname, String descr) {
		super();
		this.lineid = lineid;
		this.colname = colname;
		this.descr = descr;
	}
	private static final long serialVersionUID = 3738999143816096759L;
	@Column(nullable = false)
	@ZzyColumn(seq=255)
	private Long lineid;

	@Column(nullable = false,length = 50)
	@ZzyColumn(seq=230)
	private String colname;

	@Column(nullable = false)
	@ZzyColumn(seq=210)
	private String descr;
	
}
