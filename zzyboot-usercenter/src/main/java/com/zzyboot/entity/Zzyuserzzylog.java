package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(editright="", insertright = "", label = "User Log", deleteright = "")
public class Zzyuserzzylog extends Zzylog implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
}
