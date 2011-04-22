package com.vaannila.web;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;

import com.vaannila.domain.Comentari;



public class BookAction extends ActionSupport implements ModelDriven<Comentari>{

	private static final long serialVersionUID = -9113041734859241965L;
	private HashMap<String,String> bookList = new HashMap<String,String>();
	private Comentari comment = new Comentari();
	
	private List <Comentari> commentList = new ArrayList<Comentari>();

	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	private String id = null;
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String addMark(){
		
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

		comment.setUsername("rodonako");

		//comment.setIsbn("0001");
		
		
		comentariDAO.saveComentari(comment);
		
		this.commentList = comentariDAO.getComentariList(comment.getIsbn());
		return SUCCESS;
	}
	
	public HashMap<String, String> getBookList() {
		return bookList;
	}

	public void setBookList(HashMap<String, String> bookList) {
		this.bookList = bookList;
	}

	public List<Comentari> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comentari> commentList) {
		this.commentList = commentList;
	}

	public String show(){
		
		//El id es el isbn del llibre actual
		//cal anar al ws i aconseguir la resta d'informacio

		bookList.put("titol", "La Biblia");
		bookList.put("autor", "UnDonNadie");
		bookList.put("any", "FaTemps");
		bookList.put("isbn",this.id);
		
		//Cal mirar a la base de dades si tenim una puntuacio associada a aquest llibre
		//Si no la tenim cal inicialitzar-la
		bookList.put("puntuacio", "5");
		
		//Agafem els comentaris associats a aquest isbn
		this.commentList = comentariDAO.getComentariList(this.id);

		
		return SUCCESS;
	}

	@Override
	public Comentari getModel() {
		return this.comment;
	}

}
