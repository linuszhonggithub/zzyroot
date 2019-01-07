package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(showright = "owner", editright="owner", insertright = "super", label = "User", deleteright = "super")
public class Zzyuser extends ZzyEntityParent implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
	@Column(unique = true,nullable = false,length = 50)
	@ZzyColumn(isprimkey = true, seq=250, editright = "super", isfrozen =  true)
	private String username;
	@Column(nullable = false)
	@ZzyColumn(fieldtype = "email",iskeyword = true, seq=240, editright = "owner")
	private String email;
	@Column(nullable = false,length = 50)
	@ZzyColumn(fieldtype = "phone",iskeyword = true, seq=230, editright = "owner")
	private String cell;
	@Column(nullable = false)
	@ZzyColumn(fieldtype = "password", seq=210, showright = "none")
	private String password;
	@Column(nullable = false)
	@ZzyColumn(fieldtype = "url", seq=220, showright = "super")
	private String url;
	@ZzyColumn(fieldtype = "int", seq=220, showright = "super")
	private Long parentid;
}
