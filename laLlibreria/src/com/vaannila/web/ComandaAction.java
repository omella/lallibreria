package com.vaannila.web;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import java.util.Vector;
import org.apache.axis.encoding.Base64;
import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.dao.UserDAO;
import com.vaannila.dao.UserDAOImpl;
import com.vaannila.domain.Mail;
import com.vaannila.domain.Usuari;

import com.vaannila.ws.GestorMail;
import com.vaannila.ws.GestorXML;
import com.vaannila.ws.GestorFirma;




public class ComandaAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = -3501832474739896258L;
	
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
	
	@SuppressWarnings("rawtypes")
	private Map session = ActionContext.getContext().getSession();;
	
	@SuppressWarnings("unchecked")
	private HashMap<String,String> bookList = (HashMap<String,String>)this.session.get("bookList");
	
	@SuppressWarnings("unchecked")
	public String add()
	{
		
		//this.bookList=(HashMap<String,String>)this.session.get("bookList");
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

		
		//FALTARIA ENVIAR EL MAIL
		
		String to = "mrodon536@gmail.com";
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
        		this.msg = this.msg + "<br>Títol " + (i+1) + " = " + comanda.get(i).get("titol") + " ---> Quantitat = " + comanda.get(i).get("num");
        	}
        	
    		Mail mail = new Mail();
    		//FALTARIA FIRMA DIGITALMENT EL COS DEL MAIL
    		mail.setData(new Date());
    		mail.setAsuptme(subject);
    		String ident = (String) this.session.get("ident");
    		mail.setDesti(to);
    		UserDAO usuariDAO = new UserDAOImpl();
    		//Usuari usuari = usuariDAO.getUser(ident);
    		//mail.setOrigen(usuari.getMail());
    		mail.setCos(body+" "+firma+" "+codi);
    		
    		mailDAO.saveMail(mail);
    		
    		GestorMail.getInstancia().enviarMailUsuari(to, subject, msg, codi);

        }
        this.session.put("comanda", null);
		return SUCCESS;
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
