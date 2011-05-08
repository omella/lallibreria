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
	private String name;
	private String password;
	private String gender;
	private String country;
	private String aboutYou;
	private boolean mailingList;
	
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
	
	@Column(name="USER_PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="USER_GENDER")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="USER_COUNTRY")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name="USER_ABOUT_YOU")
	public String getAboutYou() {
		return aboutYou;
	}
	public void setAboutYou(String aboutYou) {
		this.aboutYou = aboutYou;
	}
	
	@Column(name="USER_MAILING_LIST")
	public Boolean getMailingList() {
		return mailingList;
	}
	public void setMailingList(Boolean mailingList) {
		this.mailingList = mailingList;
	}

}
