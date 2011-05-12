package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibreria;

public interface LlibreriaDAO {

	public void saveLlibreria(Llibreria llibreria);
	public List<Llibreria> listLlibreria();
	public boolean existLlibreria(String llibreriaId, String passwd);
	List<Cupo> getCuponsLlibreria();
	public Llibreria getLlibreriaMail(String id);

}
