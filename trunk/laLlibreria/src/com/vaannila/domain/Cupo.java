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
		
	@Column(name="LLIBRERIA_CUPO")
	private String llibreria_cupo;
    
	private String llibreria_name;//no mapeada en la BD
	
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


	public String getLlibreria_cupo() {
		return llibreria_cupo;
	}

	public void setLlibreria_cupo(String llibreria_cupo) {
		this.llibreria_cupo = llibreria_cupo;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public void setLlibreria_name(String llibreria_name) {
		this.llibreria_name = llibreria_name;
	}

	public String getLlibreria_name() {
		return llibreria_name;
	}

}
