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

import org.apache.struts2.interceptor.SessionAware;
import org.xml.sax.SAXException;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.dao.PuntuacioDAO;
import com.vaannila.dao.PuntuacioDAOImpl;
import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.Comentari;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Puntuacio;
import com.vaannila.domain.Vist;
import com.vaannila.ws.BooksWS;;



public class BookAction extends ActionSupport implements ModelDriven<Comentari>, SessionAware{

	private static final long serialVersionUID = -9113041734859241965L;
	private HashMap<String,String> bookList = new HashMap<String,String>();
	private Comentari comment = new Comentari();
	private Vist viewed = new Vist();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl(); 
	private List<Llibreria>llibreriaList = null;
	private List<String>llibreriesNoms = null;
	private Puntuacio puntuacio = new Puntuacio();
	
	private List <Comentari> commentList = new ArrayList<Comentari>();

	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	
	private PuntuacioDAO puntuacioDAO = new PuntuacioDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	private String id = null;
	private Double punts;
	
	private Map session;
	
	private Llibre llibre = new Llibre();

	public Llibre getLlibre() {
		return llibre;
	}

	public void setLlibre(Llibre llibre) {
		this.llibre = llibre;
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

	public String addMark(){
		
		this.puntuacio = puntuacioDAO.getPuntuacioIsbn(this.id);
		
		if (this.puntuacio == null)
		{
			this.puntuacio = new Puntuacio();
			this.puntuacio.setIsbn(this.id);
			this.puntuacio.setNumVots(1);
			this.puntuacio.setPuntuacio(this.punts);
		}
		else
		{
			puntuacio.setPuntuacio(((puntuacio.getPuntuacio()*puntuacio.getNumVots())+this.punts)/(puntuacio.getNumVots()+1));
			puntuacio.setNumVots(puntuacio.getNumVots()+1);
		}
		puntuacioDAO.savePuntuacio(puntuacio);
		
		this.setLlibre(com.vaannila.ws.BooksWS.getBook(this.id));
		bookList.put("puntuacio", this.unDecimal(puntuacio.getPuntuacio()).toString());
		bookList.put("numVots", puntuacio.getNumVots().toString());		
		this.commentList = comentariDAO.getComentariList(this.id);
		
		return SUCCESS;
	}
	
	public Comentari getComment() {
		return this.comment;
	}

	public void setComment(Comentari comment) {
		this.comment = comment;
	}

	public String addComment(){
		
		Date data = new Date();
		
		comment.setData(data);
		Map session = ActionContext.getContext().getSession();
	    String username = null;
	    username= (String) session.get("username");
	    if (username==null)username = "rodonako";
		comment.setUsername(username);

		//comment.setIsbn("0001");
		
		
		comentariDAO.saveComentari(comment);
		this.setId(comment.getIsbn());
		
		this.setLlibre(com.vaannila.ws.BooksWS.getBook(this.id));
		this.puntuacio = puntuacioDAO.getPuntuacioIsbn(this.id);
		if (puntuacio!=null) 
		{
			
			bookList.put("puntuacio", this.unDecimal(puntuacio.getPuntuacio()).toString());
			bookList.put("numVots", puntuacio.getNumVots().toString());
		}
		//this.commentList = comentariDAO.getComentariList(this.id);
		this.commentList = comentariDAO.getComentariList(comment.getIsbn());
		return SUCCESS;
	}
	
	public HashMap<String, String> getBookList() {
		return bookList;
	}

	public void setBookList(HashMap<String, String> bookList) {
		this.session = ActionContext.getContext().getSession();
		this.session.put("bookList",bookList);
		this.bookList = bookList;
	}

	public List<Comentari> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comentari> commentList) {
		this.commentList = commentList;
	}

	private Double unDecimal(Double x)
	{
		Double p = x*10;
		p = (double) Math.round(p);
		p = p/10;
		return p;
	}
	public String show() throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, ParserConfigurationException, SAXException, IOException{
		
		Date data = new Date();
		viewed.setData(data);
		viewed.setIsbn(this.id);
		vistDAO.saveVist(viewed);
		
		this.setLlibre(com.vaannila.ws.BooksWS.getBook(this.id));
		

		//Cal mirar a la base de dades si tenim una puntuacio associada a aquest llibre
		//Si no la tenim cal inicialitzar-la
	
		puntuacio = puntuacioDAO.getPuntuacioIsbn(this.id);
		if (puntuacio!=null) 
		{
			bookList.put("puntuacio", this.unDecimal(puntuacio.getPuntuacio()).toString());
			bookList.put("numVots", puntuacio.getNumVots().toString());
		}
		//Agafem els comentaris associats a aquest isbn
		this.commentList = comentariDAO.getComentariList(this.id);

		this.llibreriaList = llibreriaDAO.listLlibreria();
		
		session.put("llibreries", llibreriaList);
		llibreriesNoms = new ArrayList<String>();
		for (int k = 0;k < llibreriaList.size();++k)
		{
			llibreriesNoms.add(llibreriaList.get(k).getName());
		}
		session.put("llibreriesNoms", llibreriesNoms);
		session.put("llibre", llibre);
		
		return SUCCESS;
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

}
