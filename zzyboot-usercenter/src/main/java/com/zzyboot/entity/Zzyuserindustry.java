package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(showright = "owner", editright="owner", insertright = "super", label = "User Industry", deleteright = "super")
public class Zzyuserindustry extends ZzyEntityParent implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
	@Column(nullable = false)
	@ZzyColumn(fieldtype = "int", seq=250, showright = "super")
	private Long userid;

	@Column(nullable = false)
	@ZzyColumn(fieldtype = "drop", label="Industry", seq=220, showright = "super", droplist = "zzyindustry.industryname", isfrozen = true)
	private Long industryid;

	@Column
	@ZzyColumn(fieldtype = "url", seq=210, showright = "super")
	private String url;
}
