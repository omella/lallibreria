package com.vaannila.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Cerca;


public class searchDAOImpl implements searchDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	public void saveCerca(Cerca cerca) {
		try {
			session.save(cerca);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

}
