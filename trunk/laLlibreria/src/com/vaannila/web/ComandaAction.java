package com.vaannila.web;

import java.util.ArrayList;
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
		Vector<ParameterMap<String,String> > comanda = (Vector<ParameterMap<String, String> >) session.get("comanda");
		List<String> listTo = (List<String>) session.get("listTo");
		if (comanda == null)
		{
			listTo = new ArrayList<String>();
			comanda = new Vector <ParameterMap<String,String> >();
		}
		if (Integer.valueOf(num) < 0) return "ERROR";
		ParameterMap<String,String> llibre = new ParameterMap <String, String>();
		llibre.put("isbn", this.bookList.get("isbn"));
		llibre.put("titol", this.bookList.get("titol"));
		llibre.put("num", this.num);
		//AGAFAR DE LA LLIBREIA ESCOLLIDA EL DESCOMPTE CORRESPONENT
		llibre.put("llibreria", "NomLlibreria");
		llibre.put("descompte", "0.0");
		String llibreria = "NomLlibreria";

		if (!listTo.contains(llibreria)) 
	    {
	    	listTo.add("llibreria");
	    }
		comanda.add(llibre);
		
		session.put("comanda",comanda);
		session.put("listTo",listTo);
		this.msg = "S'ha afegit la comanda al teu carret de la compra." +
				" Prem ENVIAR per comfirmar l'enviament de la comanda";
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String send()
	{
		Vector<ParameterMap<String,String> > comanda = (Vector<ParameterMap<String, String> >) session.get("comanda");
        if (comanda == null)
        {
        	this.msg = "S'ha produït un error al afegir la comanda al carret de la compra. Disculpeu les molèsties";
        }
        else
        {
		
			String subject = "LaLlibreria.cat";
			
			List<String> listTo = (List<String>) session.get("listTo");
			for (int i = 0; i < listTo.size(); ++i)
			{
				Vector<ParameterMap<String,String> > enviament = new Vector<ParameterMap<String,String> >();
				for (int j = 0; j < comanda.size(); ++j)
				{
					if (listTo.get(i)== comanda.get(j).get("llibreria"))
					{
						enviament.add(comanda.get(j));
					}
				}
				String to = "mrodon536@gmail.com";
				//String to = mail de la llibreria
      

	        	String body = GestorXML.createDocument(enviament);
	        	
	        	String firma = Base64.encode(body.getBytes());
	        	
	        	firma = GestorFirma.signar(firma);
        	
	        	Integer codi = firma.hashCode(); 
	
	        	GestorMail.getInstancia().enviarMail(to, subject,body,firma,codi.toString());
	        	
	        	this.msg = "S'ha enviat correctament. Aquests són els detalls de la comanda:<br>";
	        	for (int k = 0; k < comanda.size();++k)
	        	{
	        		this.msg = this.msg + "<br>Títol " + (k+1) + " = " + comanda.get(k).get("titol") + " ---> Quantitat = " + comanda.get(k).get("num");
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
	    		mail.setCos(body+" "+firma);
	    		mail.setCodiReserva(codi);
	    		mailDAO.saveMail(mail);
	    		
	    		GestorMail.getInstancia().enviarMailUsuari(to, subject, msg, codi);

			}
			this.session.put("comanda", null);
        }
        
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
