package com.vaannila.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Mail;

public class MailDAOImpl implements MailDAO{

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	@Override
	public void saveMail(Mail mail) {
		try {
			session.save(mail);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}
	public Boolean checkCodi(String codi, String llibreria) {
		List<Mail> courses = null;
		try{
			courses = session.createQuery("from Mail where CODI='"+codi+"' AND DESTI='"+llibreria+"'").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return (courses != null && courses.size()>0);
	}
}
