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
	
	@Override
	public void deleteCupo(Cupo cupo) {
		try {
			session.delete(cupo);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cupo> listCupoLlibreria(String id_mail) {
		List<Cupo> result = null;
		try {
			result = session.createQuery("from Cupo where LLIBRERIA_CUPO='"+id_mail+"' order by VALOR desc").list();
		}
		catch(Exception e){
			
		}
		if (result != null)System.out.println(result.toString());
		else System.out.println("NO HI HA RESULTATS"+ id_mail);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cupo> listCupoTematica(String tematica) {
		List<Cupo> result = null;
		try {
			result = session.createQuery("from Cupo where TEMATICA='"+tematica+"' order by VALOR desc").list();
		}
		catch(Exception e){
			
		}
		return result;
	}

	@Override
	public String getCupoValor(String nom, String genre) {
		List<Cupo> result = null;
		try {
			result = session.createQuery("from Cupo where TEMATICA='"+genre+"' AND LLIBRERIA_CUPO='"+nom+"'").list();
		}
		catch(Exception e){
			
		}
		if (result.size() == 0) 
		{
			String genere = "generic";
			result = session.createQuery("from Cupo where TEMATICA='generic' AND LLIBRERIA_CUPO='"+nom+"'").list();
		}
		if (result.size() == 0) return "0.0";
		return result.get(0).getValor().toString();
	}

}
