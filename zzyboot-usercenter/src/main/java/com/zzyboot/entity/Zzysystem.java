package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(showright = "super", editright="super", insertright = "super", label = "System", deleteright = "super")
public class Zzysystem extends ZzyEntityParent implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
	@Column(unique = true,nullable = false,length = 50)
	@ZzyColumn(isprimkey = true, seq=250, editright = "super", isfrozen =  true)
	private String systemname;
	@Column(length = 150)
	@ZzyColumn(seq=220, editright = "super", isfrozen =  true)
	private String systemdesc;
}
