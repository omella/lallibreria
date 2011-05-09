package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.Usuari;

public interface UserDAO {

	public void saveUser(Usuari user);
	public List<Usuari> listUser();
}
