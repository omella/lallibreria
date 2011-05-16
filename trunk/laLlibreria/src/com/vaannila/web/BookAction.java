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

import com.safelayer.trustedx.client.smartwrapper.SmartDecryptResponse.thisRecipientInfo;
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
import com.vaannila.ws.BooksWS;;



public class BookAction extends ActionSupport implements ModelDriven<Comentari>, SessionAware{

	private static final long serialVersionUID = -9113041734859241965L;

	private Comentari comment = new Comentari();
	private Vist viewed = new Vist();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl(); 
	private List<Llibreria>llibreriaList = null;
	private List<String>llibreriesNoms = null;
	private List<String>llibreriesCupons = null;
	private Puntuacio puntuacio = new Puntuacio();
	private List llibreriaComanda= null;
	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private PuntuacioDAO puntuacioDAO = new PuntuacioDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	private Double punts;
	private String id = null;
	private Map session = ActionContext.getContext().getSession();
	private List<Comanda> comandes= (List<Comanda>) session.get("comandes");
	private Llibre llibre = (Llibre) session.get("llibre");
	private Usuari logged = (Usuari) session.get("user");

	
	public String addMark(){
		
		this.puntuacio = puntuacioDAO.getPuntuacioIsbn(this.llibre.getIsbn());
		
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
			
		return SUCCESS;
	}
	

	public String addComment(){
		
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
		
		llibre = com.vaannila.ws.BooksWS.getBook(this.id);
		List<Llibre> prova = com.vaannila.ws.BooksWS.getSimilarBook(this.id);
		System.out.println("El llibre "+llibre.getTitle()+" te "+prova.size()+" llibres similars");
		llibre.setSimilars(prova);
		this.setLlibre(this.llibre);
		
		this.llibreriaList = llibreriaDAO.listLlibreria();
		
		session.put("llibreries", llibreriaList);
		llibreriesNoms = new ArrayList<String>();
		llibreriesCupons = new ArrayList <String>();
		llibreriaComanda = new ArrayList<LlistaLlibreria>();
		for (int k = 0;k < llibreriaList.size();++k)
		{
			String nom = llibreriaList.get(k).getName();
			llibreriesNoms.add(nom);
			String valor = cupoDAO.getCupoValor(llibreriaList.get(k).getMail(),this.llibre.getGenre());
			llibreriesCupons.add(valor);
			LlistaLlibreria l = new LlistaLlibreria();
			l.tag =nom+" ->(Descompte: "+((Double)(Double.valueOf(valor)*100)).toString()+"%)";
			l.nom = nom;
			llibreriaComanda.add(l);
			//llibreriaComanda.put("text", nom+" ->(Descompte: "+((Double)(Double.valueOf(valor)*100)).toString()+"%)");
			//llibreriaComanda.put("name", nom);
		}
		session.put("llibreriesNoms", llibreriesNoms);
		session.put("llibre", llibre);
		session.put("llibreriesCupons", llibreriesCupons);
		session.put("llibreriaComanda", llibreriaComanda);
		System.out.println(llibreriaComanda.toString());
		
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

	
	
	public List<LlistaLlibreria> getLlibreriaComanda() {
		return llibreriaComanda;
	}


	public void setLlibreriaComanda(List<LlistaLlibreria> llibreriaComanda) {
		this.llibreriaComanda = llibreriaComanda;
	}


	@Override
	public Comentari getModel() {
		return this.comment;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session=arg0;
		
	}

}
