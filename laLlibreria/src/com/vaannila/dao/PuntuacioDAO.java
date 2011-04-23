package com.vaannila.dao;


import com.vaannila.domain.Puntuacio;

public interface PuntuacioDAO {

	public void savePuntuacio(Puntuacio puntuacio);

	public Puntuacio getPuntuacioIsbn(String id);
	
}
