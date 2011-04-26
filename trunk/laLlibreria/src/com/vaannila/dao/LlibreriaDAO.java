package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.User;

public interface LlibreriaDAO {

	public void saveLlibreria(Llibreria llibreria);
	public List<Llibreria> listLlibreria();
}
