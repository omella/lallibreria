package com.vaannila.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Vector;
import org.apache.axis.encoding.Base64;
import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.domain.Mail;

import com.vaannila.ws.GestorMail;
import com.vaannila.ws.GestorXML;
import com.vaannila.ws.GestorFirma;




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
		llibre.put("isbn", this.bookList.get("isbn"));
		llibre.put("titol", this.bookList.get("titol"));
		llibre.put("num", this.num);
		comanda.add(llibre);
		
		session.put("comanda",comanda);
		this.msg = "S'ha afegit la comanda al teu carret de la compra." +
				" Prem ENVIAR per comfirmar l'enviament de la comanda";
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
		
        String subject = "LaLlibreria.cat";
        if (comanda == null)
        {
        	//GestorMail.getInstancia().enviarMail(to, subject.toString(), "ERROR");
        	this.msg = "S'ha produït un error al afegir la comanda al carret de la compra. Disculpeu les molèsties";
        }
        else
        {
        	String body = GestorXML.createDocument(comanda);
        	
        	String firma = Base64.encode(body.getBytes());
        	
        	firma = GestorFirma.signar(firma);
        	
        	Integer codi = firma.hashCode(); 

        	GestorMail.getInstancia().enviarMail(to, subject,body,firma,codi.toString());
        	
        	this.msg = "S'ha enviat correctament. Aquests són els detalls de la comanda:<br>";
        	for (int i = 0; i < comanda.size();++i)
        	{
        		this.msg = this.msg + "<br>ISBN " + (i+1) + " = " + comanda.get(i).get("isbn") + "---> Quantitat = " + comanda.get(i).get("num");
        	}

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public HashMap<String, String> getBookList() {
		return bookList;
	}

	public void setBookList(HashMap<String, String> bookList) {
		this.bookList = bookList;
	}

	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
		
	}

}
