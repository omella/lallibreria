package com.vaannila.domain;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
@Table(name="PUNTUACIO")
public class Puntuacio {
	String isbn;
	int numVots;
	int puntuacio;
	
	@Id
	@Column(name="ISBN")
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Column(name="NOMBRE")
	public int getNumVots() {
		return numVots;
	}
	public void setNumVots(int numVots) {
		this.numVots = numVots;
	}
	
	@Column(name="PUNTS")
	public int getPuntuacio() {
		return puntuacio;
	}
	public void setPuntuacio(int puntuacio) {
		this.puntuacio = puntuacio;
	}
	
	
}
