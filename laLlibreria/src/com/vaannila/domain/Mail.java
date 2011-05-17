package com.vaannila.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotEmpty;

@Entity
@Table(name="MAIL")
public class Mail {

	private Long id;
	private String origen;
	private String desti;
	private String asuptme;
	private String cos;
	private Date data;
	private String codiReserva;
	private String vist;

	@Id
	@GeneratedValue
	@Column(name="MAIL_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="ORIGEN")
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	@Column(name="DESTI")
	public String getDesti() {
		return desti;
	}
	public void setDesti(String desti) {
		this.desti = desti;
	}
	
	@Column(name="ASUMPTE")
	public String getAsuptme() {
		return asuptme;
	}
	public void setAsuptme(String asuptme) {
		this.asuptme = asuptme;
	}
	
	@Column(name="COS")
	public String getCos() {
		return cos;
	}
	public void setCos(String cos) {
		this.cos = cos;
	}
	
	@Column(name="DATA")
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	@NotEmpty
	@Column(name="CODI")
	public String getCodiReserva() {
		return codiReserva;
	}
	public void setCodiReserva(String codiReserva) {
		this.codiReserva = codiReserva;
	}
	
	@Column(name="VIST")
	public String getVist() {
		return vist;
	}
	public void setVist(String vist) {
		this.vist = vist;
	}
		
}
