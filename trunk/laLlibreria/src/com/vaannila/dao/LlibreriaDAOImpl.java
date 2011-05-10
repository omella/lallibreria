package com.vaannila.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Llibreria;

public class LlibreriaDAOImpl implements LlibreriaDAO {
//OJO
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
//OJO
	@SuppressWarnings("unchecked")
	@Override
	public List<Llibreria> listLlibreria() {	
		List<Llibreria> courses = null;
		try {
			courses = session.createQuery("from Llibreria").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return courses;
	}

	@Override
	public boolean existLlibreria(String llibreriaId, String passwd){
		List<Llibreria> courses = null;
		try{
			courses = session.createQuery("from Llibreria where LLIBRERIA_MAIL='"+llibreriaId+"' AND password='"+passwd+"'").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return (courses != null && courses.size()>0);
	}
	
	@Override
	public void saveLlibreria(Llibreria llibreria) {
		try {
			session.save(llibreria);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}
	
	

}
