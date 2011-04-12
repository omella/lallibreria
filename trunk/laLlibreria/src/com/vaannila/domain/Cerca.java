package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CERCA")
public class Cerca {

	private Long id;
	private String key;
		
	@Id
	@GeneratedValue
	@Column(name="CERCA_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="KEY")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
