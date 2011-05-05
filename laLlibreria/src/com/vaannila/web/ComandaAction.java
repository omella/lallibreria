package com.vaannila.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

	private String idLlibre = null;
	
	public String getIdLlibre() {
		return idLlibre;
	}

	public void setIdLlibre(String idLlibre) {
		this.idLlibre = idLlibre;
	}

	private String num = null;
	
	private String msg = null;
	
	private Map session;
	
	private HashMap<String,String> bookList = new HashMap<String,String>();
	
	@SuppressWarnings("unchecked")
	public String add()
	{
		this.session = ActionContext.getContext().getSession();
		
		this.bookList=(HashMap<String,String>)this.session.get("bookList");
		Vector<ParameterMap<String,String> > comanda = (Vector<ParameterMap<String, String> >) session.get("comanda");
		if (comanda == null)
		{
			comanda = new Vector <ParameterMap<String,String> >();
		}
		ParameterMap<String,String> llibre = new ParameterMap <String, String>();
		llibre.put("isbn", this.idLlibre);
		llibre.put("num", this.num);
		comanda.add(llibre);
		
		session.put("comanda",comanda);
		this.msg = "S'ha afegit la petició a la teva comanda." +
				" Prem ENVIAR perquè la llibreria rebi la comanda";
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String send()
	{
		//FALTARIA FIRMA DIGITALMENT EL COS DEL MAIL
		this.mail.setData(new Date());
		mailDAO.saveMail(this.mail);
		
		//FALTARIA ENVIAR EL MAIL
		//String username = this.session.get("username");
		//User usuari = getUser(username);
		String to = "mrodon536@gmail.com";
		Map session = ActionContext.getContext().getSession();
		Vector<ParameterMap<String,String> > comanda = (Vector<ParameterMap<String, String> >) session.get("comanda");
		
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
        this.session.put("comanda", null);
		return SUCCESS;
	}
	public Mail getModel() {
		return mail;
	}
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
		
	}

}
