package com.vaannila.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Comentari;

public class ComentariDAOImpl implements ComentariDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@Override
	public void saveComentari(Comentari comentari){
		try {
			System.out.println(comentari.getText());
			session.save(comentari);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comentari> getComentariList(String isbn) {
		List<Comentari> result= null;
		try {
			result = session.createQuery("from Comentari where ISBN="+isbn+" order by DATA desc").list();
		}
		catch(Exception e){
			
		}
		System.out.println();
		return result;
	}

}
