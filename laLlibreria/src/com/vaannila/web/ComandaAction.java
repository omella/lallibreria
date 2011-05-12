package com.vaannila.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;
import java.util.Vector;
import org.apache.axis.encoding.Base64;
import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;

import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Mail;
import com.vaannila.domain.Usuari;

import com.vaannila.ws.GestorMail;
import com.vaannila.ws.GestorXML;
import com.vaannila.ws.GestorFirma;




public class ComandaAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = -3501832474739896258L;
	
	private MailDAO mailDAO = new MailDAOImpl();
	
	private String idLlibreria = null;
	
	private String num = null;
	
	private String msg = null;
	private LlibreriaDAO libdao = new LlibreriaDAOImpl();
	private Map session = ActionContext.getContext().getSession();
	
	private Llibre llibre = (Llibre)this.session.get("llibre");
	
	@SuppressWarnings("unchecked")
	private List<Llibreria>llibreriaList = (List<Llibreria>) this.session.get("llibreries");
	
	@SuppressWarnings("unchecked")
	private List<String>llibreriesNoms = (List<String>) this.session.get("llibreriesNoms");
	
	@SuppressWarnings("unchecked")
	private List<String>llibreriesCupons = (List<String>) this.session.get("llibreriesCupons");

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
		llibre.put("isbn", this.llibre.getIsbn());
		llibre.put("titol", this.llibre.getTitle());
		llibre.put("num", this.num);
		//AGAFAR DE LA LLIBREIA ESCOLLIDA EL DESCOMPTE CORRESPONENT
		llibre.put("llibreria", this.idLlibreria);
		//llibre.put("descompte", "0.0");
		
		
		String mail = null;

		for (int k = 0; k < llibreriaList.size(); ++k)
		{

			if (this.llibreriaList.get(k).getName().equals(idLlibreria))
			{ 
				mail = this.llibreriaList.get(k).getMail();

				llibre.put("mail", mail);
				llibre.put("descompte",llibreriesCupons.get(k));
			}
		}
			
		if (!listTo.contains(mail)) 
	    {
	    	listTo.add(mail);
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
			Usuari usuari = (Usuari) session.get("user");

			String cos_usuari = "Hola "+usuari.getName()+",\n\n" +
					"La teva comanda s'ha realitzat correctament, a continuació li llistem els codis de reserva per cada llibreria a la qual has enviat una petició.\n\n";
			String to = null;
			List<String> listTo = (List<String>) session.get("listTo");
			for (int i = 0; i < listTo.size(); ++i)
			{
				Vector<ParameterMap<String,String> > enviament = new Vector<ParameterMap<String,String> >();
				for (int j = 0; j < comanda.size(); ++j)
				{

					if (listTo.get(i)== comanda.get(j).get("mail"))
					{
						enviament.add(comanda.get(j));
					}
				}
				
				to = listTo.get(i);
				String body = GestorXML.createDocument(enviament);
				
	        	String firma = Base64.encode(body.getBytes());
	        	
	        	firma = GestorFirma.signar(firma);
        	
	        	Integer codi = firma.hashCode();
				
				for (int j = 0; j < enviament.size(); ++j)
				{
					
					String m = enviament.get(j).get("mail");
					Llibreria l = libdao.getLlibreriaMail(m);
					cos_usuari = cos_usuari + "Llibreria: "+l.getName()+" Codi: "+codi.toString()+"\n";
					if (listTo.get(i)== enviament.get(j).get("mail"))
					{
						cos_usuari = cos_usuari + "Llibre: "+enviament.get(j).get("titol")+"	Descompte: "+enviament.get(j).get("descompte")+"\n";
					}
				}
	        	
 
	
	        	String cos = "Hola, \n \n Un usuari de LALLIBRERIA.CAT ha realitzat una petició de comanda a la teva llibreria.\n" +
	        			"Codi de la reserva: "+codi.toString()+"\n\n Pots consultar més detalls de la reserva als fitxers adjunts.";
	        	GestorMail.getInstancia().enviarMail(to, subject,body,firma,cos);
	        	
	        	this.msg = "S'ha enviat correctament. Aquests són els detalls de la comanda:<br>";
	        	for (int k = 0; k < comanda.size();++k)
	        	{
	        		this.msg = this.msg + "<br>Títol " + (k+1) + " = " + comanda.get(k).get("titol") + " ---> Quantitat = " + comanda.get(k).get("num");
	        	}
        	
	    		Mail mail = new Mail();
	    		
	    		mail.setData(new Date());
	    		mail.setAsuptme(subject);
	    		mail.setDesti(to);
	    		
	    		
	    		//mail.setOrigen(usuari.getMail());
	    		mail.setOrigen("mrodon536@gmail.com");
	    		mail.setCos(body);
	    		mail.setCodiReserva(codi.toString());
	    		mailDAO.saveMail(mail);
	    		
	    		to = mail.getOrigen();


			}
			cos_usuari = cos_usuari + "\nGràcies per confiar en nosaltres.\n\nLALLIBRERIA.CAT";
			GestorMail.getInstancia().enviarMailUsuari(to, subject, cos_usuari);
			
			this.session.put("comanda", null);
			this.session.put("listTo",null);
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
	
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
		
	}

	public Llibre getLlibre() {
		return llibre;
	}

	public void setLlibre(Llibre llibre) {
		this.llibre = llibre;
	}

	public List<Llibreria> getLlibreriaList() {
		return llibreriaList;
	}

	public void setLlibreriaList(List<Llibreria> llibreriaList) {
		this.llibreriaList = llibreriaList;
	}

	public String getIdLlibreria() {
		return idLlibreria;
	}

	public void setIdLlibreria(String idLlibreria) {
		this.idLlibreria = idLlibreria;
	}

	public List<String> getLlibreriesNoms() {
		return llibreriesNoms;
	}

	public void setLlibreriesNoms(List<String> llibreriesNoms) {
		this.llibreriesNoms = llibreriesNoms;
	}
	
	

}
