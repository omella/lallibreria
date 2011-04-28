package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.Cupo;


public interface CupoDAO {

	public void saveCupo(Cupo cupo);
	public List<Cupo> listCupoLlibreria(int id);
	public List<Cupo> listCupoTematica(String tematica);

}
