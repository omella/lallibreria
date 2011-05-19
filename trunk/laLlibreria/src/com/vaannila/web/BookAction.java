package com.vaannila.web;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;
import org.xml.sax.SAXException;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.dao.PuntuacioDAO;
import com.vaannila.dao.PuntuacioDAOImpl;
import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.Comanda;
import com.vaannila.domain.Comentari;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Puntuacio;
import com.vaannila.domain.Usuari;
import com.vaannila.domain.Vist;


public class BookAction extends ActionSupport implements ModelDriven<Comentari>, SessionAware{

	private static final long serialVersionUID = -9113041734859241965L;

	private Comentari comment = new Comentari();
	private Vist viewed = new Vist();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl(); 
	private List<Llibreria>llibreriaList = null;

	private Puntuacio puntuacio = new Puntuacio();
	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private PuntuacioDAO puntuacioDAO = new PuntuacioDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	private Double punts;
	private String id = null;
	private Map session = ActionContext.getContext().getSession();
	private List<ParameterMap<String,String>> ofertes = (List<ParameterMap<String, String>>) session.get("ofertes");
	private List<Comanda> comandes= (List<Comanda>) session.get("comandes");
	private Llibre llibre = (Llibre) session.get("llibre");
	private Usuari logged = (Usuari) session.get("user");
	private Boolean voted =  (Boolean) session.get("voted");
	private List<String>llibreriesNoms = (List<String>) session.get("llibreriesNoms");
	private List<String>llibreriesCupons = (List<String>) session.get("llibreriesCupons");
	private ParameterMap<String,String> distances = (ParameterMap<String, String>) session.get("distancias");
	
	public String addMark(){
		//Si ja ha votat, acabem!
		if (voted != null && voted == true) return SUCCESS;
		this.puntuacio = puntuacioDAO.getPuntuacioIsbn(this.llibre.getIsbn());
		
		//Comprovació errors
		if (punts < 1.0) punts = 1.0;
		if (punts > 5.0) punts = 5.0;
		
		if (this.puntuacio == null)
		{
			this.puntuacio = new Puntuacio();
			this.puntuacio.setIsbn(this.llibre.getIsbn());
			this.puntuacio.setNumVots(1);
			this.puntuacio.setPuntuacio(this.punts);
		}
		else
		{
			puntuacio.setPuntuacio(((puntuacio.getPuntuacio()*puntuacio.getNumVots())+this.punts)/(puntuacio.getNumVots()+1));
			puntuacio.setNumVots(puntuacio.getNumVots()+1);
		}
		
		puntuacioDAO.savePuntuacio(puntuacio);
		
		this.setLlibre(this.llibre);
		voted = true;
		session.put("voted",voted);	
		return SUCCESS;
	}
	

	public String addComment(){
		
		Date data = new Date();		
		comment.setData(data);
	    String username = null;
	    Usuari user= (Usuari) session.get("user");
	    if (user == null) return ERROR;
	    username = user.getName();
	    if (username==null)username = "rodonako";
		comment.setUsername(username);
		comment.setIsbn(this.llibre.getIsbn());
		comentariDAO.saveComentari(comment);
		
		this.setLlibre(this.llibre);
		
		return SUCCESS;
	}
	
	public String commentsAjax(){
		Date data = new Date();		
		comment.setData(data);
	    String username = null;
	    Usuari user= (Usuari) session.get("user");
	    username = user.getName();
	    if (username==null)username = "rodonako";
		comment.setUsername(username);
		comment.setIsbn(this.llibre.getIsbn());
		comentariDAO.saveComentari(comment);
		
		this.setLlibre(this.llibre);
		
		return SUCCESS;
	}
	


	public String show() {
		
		Date data = new Date();
		viewed.setData(data);
		viewed.setIsbn(this.id);
		vistDAO.saveVist(viewed);
		if (llibre != null)
		{
			if (llibre.getIsbn() != this.id) voted = false;
		}
		llibre = com.vaannila.ws.BooksWS.getBook(this.id);
		List<Llibre> prova = com.vaannila.ws.BooksWS.getSimilarBook(this.id);
		System.out.println("El llibre "+llibre.getTitle()+" te "+prova.size()+" llibres similars");
		llibre.setSimilars(prova);
		this.setLlibre(this.llibre);
		
		this.llibreriaList = llibreriaDAO.listLlibreria();
		
		session.put("llibreries", llibreriaList);
		llibreriesNoms = new ArrayList<String>();
		llibreriesCupons = new ArrayList <String>();
		ofertes = new ArrayList<ParameterMap<String,String>>();
		for (int k = 0;k < llibreriaList.size();++k)
		{
			String nom = llibreriaList.get(k).getName();
			llibreriesNoms.add(nom);
			String valor = cupoDAO.getCupoValor(llibreriaList.get(k).getMail(),this.llibre.getGenre());
			llibreriesCupons.add(valor);
			if (Double.valueOf(valor) > 0.0) 
			{
				ParameterMap<String,String> oferta = new ParameterMap<String,String>();
				oferta.put("descompte",Double.valueOf(valor).toString()+"%");
				oferta.put("llibreria", nom);
				if (distances!=null) {
					Double km = (Double)Double.valueOf(distances.get(nom))/1000;
					km = this.unDecimal(km);
					oferta.put("distancia", km.toString()+" Km");
				}
				ofertes.add(oferta);
			}
		}
		session.put("voted", voted);
		session.put("llibreriesNoms", llibreriesNoms);
		session.put("llibre", llibre);
		session.put("llibreriesCupons", llibreriesCupons);
		session.put("ofertes",ofertes);
				
		return SUCCESS;
	}
	class LlistaLlibreria{
		String tag;
		String nom;
		
	}

	public Llibre getLlibre() {
		return llibre;
	}

	public void setLlibre(Llibre llibre) {
		this.llibre = llibre;
		this.llibre.setCommentList(comentariDAO.getComentariList(this.llibre.getIsbn()));
		Puntuacio punt = puntuacioDAO.getPuntuacioIsbn(this.llibre.getIsbn());
		if(punt==null) {
			this.llibre.setNumVots(null);
			this.llibre.setPuntuacio(null);
		}
		else {
			this.llibre.setNumVots(puntuacioDAO.getPuntuacioIsbn(this.llibre.getIsbn()).getNumVots().toString());
			this.llibre.setPuntuacio(this.unDecimal(puntuacioDAO.getPuntuacioIsbn(this.llibre.getIsbn()).getPuntuacio()).toString());
		}
	}

	private Double unDecimal(Double x)
	{
		if (x!=null) {
			Double p = x*10;
			p = (double) Math.round(p);
			p = p/10;
			return p;
		}
		return null;
	}
	public Puntuacio getPuntuacio() {
		return puntuacio;
	}

	public void setPuntuacio(Puntuacio puntuacio) {
		this.puntuacio = puntuacio;
	}

	public Double getPunts() {
		return punts;
	}

	public void setPunts(Double punts) {
		this.punts = punts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Comentari getComment() {
		return this.comment;
	}

	public void setComment(Comentari comment) {
		this.comment = comment;
	}
	
	public List<Llibreria> getLlibreriaList() {
		return llibreriaList;
	}

	public void setLlibreriaList(List<Llibreria> llibreriaList) {
		this.llibreriaList = llibreriaList;
	}

	public List<String> getLlibreriesNoms() {
		return llibreriesNoms;
	}

	public void setLlibreriesNoms(List<String> llibreriesNoms) {
		this.llibreriesNoms = llibreriesNoms;
	}

	@Override
	public Comentari getModel() {
		return this.comment;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session=arg0;
		
	}

	public List<ParameterMap<String, String>> getOfertes() {
		return ofertes;
	}


	public void setOfertes(List<ParameterMap<String, String>> ofertes) {
		this.ofertes = ofertes;
	}


	public List<Comanda> getComandes() {
		return comandes;
	}


	public void setComandes(List<Comanda> comandes) {
		this.comandes = comandes;
	}


	public Usuari getLogged() {
		return logged;
	}


	public void setLogged(Usuari logged) {
		this.logged = logged;
	}


	public Boolean getVoted() {
		return voted;
	}


	public void setVoted(Boolean voted) {
		this.voted = voted;
	}


	public ParameterMap<String, String> getDistance() {
		return distances;
	}


	public void setDistance(ParameterMap<String, String> distance) {
		this.distances = distance;
	}



	
	
}
