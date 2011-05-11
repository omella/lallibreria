package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.Usuari;

public interface UserDAO {
	public boolean existUser(String serviceId, Boolean tipus);
	public void saveUser(Usuari user);
	public List<Usuari> listUser();
	public Usuari getUser(String serviceId, Boolean isGoogleAccount);
}
