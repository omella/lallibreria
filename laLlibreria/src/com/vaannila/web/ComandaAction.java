package com.vaannila.web;



import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;


import com.vaannila.domain.Comentari;
import com.vaannila.domain.Mail;



public class ComandaAction extends ActionSupport implements ModelDriven<Mail>{

	private static final long serialVersionUID = -3501832474739896258L;
	
	private Mail mail = new Mail();
	
	private List <Comentari> commentList = new ArrayList<Comentari>();

	private MailDAO mailDAO = new MailDAOImpl();

	
	public String add()
	{
		//FALTARIA FIRMA DIGITALMENT EL COS DEL MAIL
		this.mail.setData(new Date());
		mailDAO.saveMail(this.mail);
		
		//FALTARIA ENVIAR EL MAIL
		
		return SUCCESS;
	}
	@Override
	public Mail getModel() {
		return mail;
	}
	

}
