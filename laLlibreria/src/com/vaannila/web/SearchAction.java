package com.vaannila.web;

import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.searchDAO;
import com.vaannila.dao.searchDAOImpl;
import com.vaannila.domain.Cerca;




public class SearchAction extends ActionSupport implements ModelDriven<Cerca>{


	private static final long serialVersionUID = -4978596336944380865L;
	private Cerca cerca = new Cerca();
	private ArrayList<String> results = new ArrayList<String>();
	private searchDAO searchDAO = new searchDAOImpl();
	
	


	public String list()
	{
		//searchDAO.saveCerca(cerca);
		System.out.println("ABANS: " + cerca.getKey());
		return SUCCESS;
	}
	
	public String show()
	{
		return SUCCESS;
	}
	
	public ArrayList<String> getResults() {
		return this.results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}

	@Override
	public Cerca getModel() {
		return cerca;
	}
}
