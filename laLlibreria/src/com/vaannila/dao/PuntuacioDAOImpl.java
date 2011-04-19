package com.vaannila.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Puntuacio;

public class PuntuacioDAOImpl implements PuntuacioDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@Override
	public void savePuntuacio(Puntuacio puntuacio) {
		try {
			session.save(puntuacio);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

}
