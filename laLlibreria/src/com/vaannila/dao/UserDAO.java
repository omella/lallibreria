package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.Usuari;

public interface UserDAO {
	public boolean existUser(String serviceId, String tipus);
	public void saveUser(Usuari user);
	public List<Usuari> listUser();
	public Usuari getUser(String serviceId, String isGoogleAccount);
}
