package com.vaannila.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Cupo;

public class CupoDAOImpl implements CupoDAO{

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	@Override
	public void saveCupo(Cupo cupo) {
		try {
			session.save(cupo);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cupo> listCupoLlibreria(int id) {
		List<Cupo> result = null;
		try {
			result = session.createQuery("from Cupo where LLIBRERIA="+id+" order by VALOR desc").list();
		}
		catch(Exception e){
			
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cupo> listCupoTematica(String tematica) {
		List<Cupo> result = null;
		try {
			result = session.createQuery("from Cupo where TEMATICA="+tematica+" order by VALOR desc").list();
		}
		catch(Exception e){
			
		}
		return result;
	}

}
