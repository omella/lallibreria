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
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean existUser(String serviceId, Boolean tipus) {	
		List<Usuari> courses = null;
		try {
			courses = session.createQuery("from Usuari Where IS_GOOGLE_ACCOUNT="+tipus + " AND SERVICE_ID="+serviceId).list();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return (courses != null && courses.size()>0);
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
	public Usuari getUser(String serviceId, Boolean isGoogleAccount){
		List<Usuari> courses = null;
	try {
		courses = session.createQuery("from Usuari Where IS_GOOGLE_ACCOUNT="+isGoogleAccount + " AND SERVICE_ID="+serviceId).list();
	} catch (Exception e) {
		//e.printStackTrace();
	}
	
	return courses.get(0);
	}
}
