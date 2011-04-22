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
	private String isbn = null;
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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
		System.out.println(comment.getText());
		Date data = new Date();
		
		comment.setData(data);
		System.out.println(comment.getData());
		comment.setUsername("rodonako");
		System.out.println(comment.getUsername());
		comment.setIsbn(this.isbn);
		System.out.println(comment.getIsbn());
		comentariDAO.saveComentari(comment);
		this.commentList.add(comment);
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
		
		//Aqui disposem de l'isbn que ha demanat l'usuari
		//cal anar al ws i aconseguir la resta d'informacio
		
		System.out.println(this.isbn);
		
		bookList.put("titol", "La Biblia");
		bookList.put("autor", "UnDonNadie");
		bookList.put("any", "FaTemps");
		
		//Cal mirar a la base de dades si tenim una puntuacio associada a aquest llibre
		//Si no la tenim cal inicialitzar-la
		bookList.put("puntuacio", "5");
		
		//Agafem els comentaris associats a aquest isbn
		this.commentList = comentariDAO.getComentariList(this.isbn);
		
		Comentari c = new Comentari();
		c.setText("AIXO ES UNA PROVA");
		this.commentList.add(c);
		
		return SUCCESS;
	}

	@Override
	public Comentari getModel() {
		return this.comment;
	}

}
