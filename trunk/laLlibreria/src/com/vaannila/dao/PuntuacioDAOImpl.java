package com.vaannila.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Comentari;
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

	@SuppressWarnings("unchecked")
	@Override
	public Puntuacio getPuntuacioIsbn(String isbn) {
		List <Puntuacio> result= null;
		try {
			result = session.createQuery("from Puntuacio where ISBN='"+isbn+"'").list();
			if (result != null) return result.get(0);
			return null;
		}
		catch(Exception e){
			
		}
		return null;
	}

}
