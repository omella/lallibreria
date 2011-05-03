package com.vaannila.web;

import java.util.Date;
import java.util.Map;

import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.domain.Mail;
import com.vaannila.ws.GestorMail;



public class ComandaAction extends ActionSupport implements ModelDriven<Mail>, SessionAware{

	private static final long serialVersionUID = -3501832474739896258L;
	
	private Mail mail = new Mail();
	
	private MailDAO mailDAO = new MailDAOImpl();

	private String id = null;
	
	private String num = null;
	
	private String msg = null;
	
	private Map Session;
	
	@SuppressWarnings("unchecked")
	public String add()
	{
		this.Session = ActionContext.getContext().getSession();
		ParameterMap<String,String> comanda = (ParameterMap<String, String>) Session.get("comanda");
		if (comanda == null)
		{
			comanda = new ParameterMap<String,String>();
		}
		comanda.put("isbn", this.id);
		comanda.put("num", this.num);
		
		Session.put("comanda",comanda);
		this.msg = "S'ha afegit la petició a la teva comanda." +
				" Prem ENVIAR perquè la llibreria rebi la comanda";
		return SUCCESS;
	}
	
	public String send()
	{
		//FALTARIA FIRMA DIGITALMENT EL COS DEL MAIL
		this.mail.setData(new Date());
		mailDAO.saveMail(this.mail);
		
		//FALTARIA ENVIAR EL MAIL
		String to = "mrodon536@gmail.com";
		Map Session = ActionContext.getContext().getSession();
		ParameterMap<String,String> comanda = (ParameterMap<String, String>) Session.get("comanda");
		
        //StringBuffer body = new StringBuffer("L'enviament de mails funciona correctament");
        //body.append(nomEsdeveniment);
        //body.append("' ha estat clausurat.\n\tCordialment,\n\tEl Planificador de PROP.\n");
		
		
        StringBuffer subject = new StringBuffer("PROVA");
        if (comanda == null)
        {
        	GestorMail.getInstancia().enviarMail(to, subject.toString(), "ERROR");
        }
        else
        {
        	GestorMail.getInstancia().enviarMail(to, subject.toString(), comanda.toString());
        }
		return SUCCESS;
	}
	@Override
	public Mail getModel() {
		return mail;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

}
