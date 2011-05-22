package com.vaannila.domain;

public class Comanda {

	private Llibre llibre;
	private String llibreria;
	private String quantitat;
	private String descompte;
	private Integer id;
	
	public Llibre getLlibre() {
		return llibre;
	}
	public void setLlibre(Llibre llibre) {
		this.llibre = llibre;
	}
	public String getLlibreria() {
		return llibreria;
	}
	public void setLlibreria(String llibreria) {
		this.llibreria = llibreria;
	}
	public String getQuantitat() {
		return quantitat;
	}
	public void setQuantitat(String quantitat) {
		this.quantitat = quantitat;
	}
	public String getDescompte() {
		return descompte;
	}
	public void setDescompte(String descompte) {
		this.descompte = descompte;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
