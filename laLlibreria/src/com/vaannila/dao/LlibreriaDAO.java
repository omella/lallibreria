package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.User;

public interface LlibreriaDAO {

	public void saveUser(User user);
	public List<User> listUser();
}
