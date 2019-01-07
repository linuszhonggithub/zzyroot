package com.zzyboot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
@ZzyTable(editright="none", insertright = "none", label = "Industry Log", deleteright = "none")
public class Zzyindustryzzylog extends Zzylog implements Serializable {
	private static final long serialVersionUID = 3738999143816096759L;
}
