package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.Llibreria;

public interface LlibreriaDAO {

	public void saveLlibreria(Llibreria llibreria);
	public List<Llibreria> listLlibreria();
}
