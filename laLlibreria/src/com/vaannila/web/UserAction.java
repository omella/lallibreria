package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.UserDAO;
import com.vaannila.dao.UserDAOImpl;
import com.vaannila.domain.Usuari;

public class UserAction extends ActionSupport implements ModelDriven<Usuari> {

	private static final long serialVersionUID = -6659925652584240539L;

	private Usuari user = new Usuari();
	private List<Usuari> userList = new ArrayList<Usuari>();
	private UserDAO userDAO = new UserDAOImpl();
	
	@Override
	public Usuari getModel() {
		return user;
	}
	
	public String add()
	{
		userDAO.saveUser(user);
		return SUCCESS;
	}
	
	public String list()
	{
		userList = userDAO.listUser();
		return SUCCESS;
	}

	public Usuari getUser() {
		return user;
	}

	public void setUser(Usuari user) {
		this.user = user;
	}

	public List<Usuari> getUserList() {
		return userList;
	}

	public void setUserList(List<Usuari> userList) {
		this.userList = userList;
	}

}
