package com.vaannila.dao;

import com.vaannila.domain.Mail;


public interface MailDAO {

	public void saveMail(Mail mail);
	public String checkCodi(String codi, String llibreria);
}
