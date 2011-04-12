package com.vaannila.web;

import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.vaannila.dao.searchDAO;
import com.vaannila.domain.Llibre;


public class SearchAction extends ActionSupport{


	private static final long serialVersionUID = -4978596336944380865L;

	private ArrayList<String> results = new ArrayList<String>();
	
	private Llibre book = new Llibre();
		

	public String list()
	{
		results = searchDAO.listResults();
		return SUCCESS;
	}
	
	public String show()
	{
		book = searchDAO.getInfoLlibre();
		return SUCCESS;
	}
	
	public ArrayList<String> getResults() {
		return this.results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}

	public Llibre getBook() {
		return book;
	}

	public void setBook(Llibre book) {
		this.book = book;
	}
}
