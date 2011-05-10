package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USUARI")
public class Usuari {

	private Long id;
	private boolean isGoogleAccount;
	private String serviceId;
	private String name;
	private String gender;
	private String mail;
	
	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="IS_GOOGLE_ACCOUNT")
	public Boolean getIsGoogleAccount() {
		return isGoogleAccount;
	}
	public void setIsGoogleAccount(boolean google) {
		this.isGoogleAccount = google;
	}
	
	@Column(name="USER_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="SERVICE_ID")
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Column(name="USER_GENDER")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="USER_MAIL")
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

}
