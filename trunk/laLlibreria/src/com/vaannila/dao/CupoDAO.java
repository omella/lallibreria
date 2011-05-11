package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.Cupo;


public interface CupoDAO {

	public void saveCupo(Cupo cupo);
	public List<Cupo> listCupoLlibreria(String id_mail);
	public List<Cupo> listCupoTematica(String tematica);
	public String getCupoValor(String nom, String genre);

}
