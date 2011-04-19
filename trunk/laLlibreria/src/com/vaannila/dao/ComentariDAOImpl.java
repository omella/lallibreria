package com.vaannila.dao;



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
			session.save(comentari);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

}
