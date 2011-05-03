package com.vaannila.web;

import java.util.Date;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.domain.Mail;
import com.vaannila.ws.GestorMail;



public class ComandaAction extends ActionSupport implements ModelDriven<Mail>{

	private static final long serialVersionUID = -3501832474739896258L;
	
	private Mail mail = new Mail();
	
	private MailDAO mailDAO = new MailDAOImpl();

	
	public String add()
	{
		//FALTARIA FIRMA DIGITALMENT EL COS DEL MAIL
		this.mail.setData(new Date());
		mailDAO.saveMail(this.mail);
		
		//FALTARIA ENVIAR EL MAIL
		String to = "mrodon536@gmail.com";
        
        StringBuffer body = new StringBuffer("Això es una prova");
        //body.append(nomEsdeveniment);
        //body.append("' ha estat clausurat.\n\tCordialment,\n\tEl Planificador de PROP.\n");

        StringBuffer subject = new StringBuffer("PROVA");

		GestorMail.getInstancia().enviarMail(to, subject.toString(), body.toString());
		
		return SUCCESS;
	}
	@Override
	public Mail getModel() {
		return mail;
	}
	

}
