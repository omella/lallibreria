package com.vaannila.domain;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
@Table(name="PUNTUACIO")
public class Puntuacio {
	String isbn;
	Integer numVots;
	Integer puntuacio;
	
	@Id
	@Column(name="ISBN")
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Column(name="NOMBRE")
	public Integer getNumVots() {
		return numVots;
	}
	public void setNumVots(Integer numVots) {
		this.numVots = numVots;
	}
	
	@Column(name="PUNTS")
	public Integer getPuntuacio() {
		return puntuacio;
	}
	public void setPuntuacio(Integer puntuacio) {
		this.puntuacio = puntuacio;
	}
	
	
}
