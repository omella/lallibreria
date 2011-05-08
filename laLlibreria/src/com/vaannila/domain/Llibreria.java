package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotEmpty;

@Entity
@Table(name="LLIBRERIA")
public class Llibreria {

	private Long id;
	private String name;
	private String mail;
	private String phone;
	private String place;
	private String cif;
	private String password;

	@Id
	@GeneratedValue
	@Column(name="LLIBRERIA_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty(message="Nom no pot ser buit")
	@Column(name="LLIBRERIA_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="LLIBRERIA_MAIL")
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Column(name="LLIBRERIA_PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="LLIBRERIA_PLACE")
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	@Column(name="LLIBRERIA_CIF")
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	
	@Column(name="LLIBRERIA_PASSWORD")
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
}
