package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.Comentari;

public interface ComentariDAO {

	public void saveComentari(Comentari comentari);

	public List<Comentari> getComentariList(String isbn);
}
