package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(showright = "owner", editright="owner", insertright = "super", label = "User", deleteright = "super")
public class Zzyuserzzynote extends Zzynote implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
}
