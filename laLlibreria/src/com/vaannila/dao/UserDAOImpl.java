package com.vaannila.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Usuari;

public class UserDAOImpl implements UserDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuari> listUser() {	
		List<Usuari> courses = null;
		try {
			courses = session.createQuery("from Usuari").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return courses;
	}

	@Override
	public void saveUser(Usuari user) {
		try {
			session.save(user);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean existeix (Usuari user){
		boolean b = false;
		try {
			List<Usuari> courses = session.createSQLQuery("Select * From USUARI Where USUARI.USER_ID == "+user.getId()+" and USUARI.IS_GOOGLE_ACCOUNT == "+user.getIsGoogleAccount()).list();
			b = !courses.isEmpty();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
}
