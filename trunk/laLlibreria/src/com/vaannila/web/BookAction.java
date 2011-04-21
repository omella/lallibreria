package com.vaannila.web;


import java.util.HashMap;

import com.opensymphony.xwork2.ActionSupport;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.BookDAO;
import com.vaannila.dao.BookDAOImpl;
import com.vaannila.domain.Comentari;



public class BookAction extends ActionSupport{

	private static final long serialVersionUID = -9113041734859241965L;
	private HashMap<String,String> bookList = new HashMap<String,String>();
	private Comentari comment = new Comentari();
	private BookDAO bookDAO = new BookDAOImpl();
	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	private String isbn;
	
	
	public String addMark(){
		
		return SUCCESS;
	}
	
	public String addComment(){
		
		comment.setIsbn(this.isbn);
		comentariDAO.saveComentari(comment);
		return SUCCESS;
	}
	
	public HashMap<String, String> getBookList() {
		return bookList;
	}

	public void setBookList(HashMap<String, String> bookList) {
		this.bookList = bookList;
	}

	public String show(){
		
		//BookList te tota la informacio de volem mostrar sobre el llibre (titol, titol llarg, autor, any, descripcio)
		//bookList = bookDAO.getInfoBook(this.isbn);
		return SUCCESS;
	}

//	@Override
//	public Llibre getModel() {
//		return book;
//	}
}
