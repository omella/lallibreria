package com.vaannila.domain;

import javax.persistence.*;

@Entity
@Table(name="CUPO")
public class Cupo {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;
	
	@Column(name="VALOR")
	private Double valor;  
		
	@Column(name="LLIBRERIA")
	private String llibreria;
    
	@Column(name="TEMATICA")
    private String tematica;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getLlibreria() {
		return llibreria;
	}

	public void setLlibreria(String llibreria) {
		this.llibreria = llibreria;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}


}
