package com.vaannila.web;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.PuntuacioDAO;
import com.vaannila.dao.PuntuacioDAOImpl;
import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.Comentari;
import com.vaannila.domain.Puntuacio;
import com.vaannila.domain.Vist;



public class BookAction extends ActionSupport implements ModelDriven<Comentari>, SessionAware{

	private static final long serialVersionUID = -9113041734859241965L;
	private HashMap<String,String> bookList = new HashMap<String,String>();
	private Comentari comment = new Comentari();
	private Vist viewed = new Vist();
	
	private Puntuacio puntuacio = new Puntuacio();
	
	private List <Comentari> commentList = new ArrayList<Comentari>();

	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	
	private PuntuacioDAO puntuacioDAO = new PuntuacioDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	private String id = null;
	private Double punts;
	
	private Map session;
	


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
		this.setBookList(com.vaannila.ws.ISBNdbWS.searchISBN(this.id));
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
		
		this.setBookList(com.vaannila.ws.ISBNdbWS.searchISBN(this.id));
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
	public String show(){
		
		Date data = new Date();
		viewed.setData(data);
		viewed.setIsbn(this.id);
		vistDAO.saveVist(viewed);
		
		//El id es el isbn del llibre actual
		//cal anar al ws i aconseguir la resta d'informacio
		this.setBookList(com.vaannila.ws.ISBNdbWS.searchISBN(this.id));

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

		
		return SUCCESS;
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
