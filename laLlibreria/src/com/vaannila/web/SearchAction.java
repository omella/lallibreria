package com.vaannila.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.ModelDriven;
//import com.vaannila.dao.searchDAO;
//import com.vaannila.dao.searchDAOImpl;
//import com.vaannila.domain.Cerca;
import com.vaannila.domain.Llibre;




//public class SearchAction extends ActionSupport implements ModelDriven<Cerca>{
public class SearchAction extends ActionSupport {

	private static final long serialVersionUID = -4978596336944380865L;
	//private Cerca cerca = new Cerca();
	
	//private searchDAO searchDAO = new searchDAOImpl();
	private List<Llibre> results = new ArrayList<Llibre>();
	private String msg = null;
	private String key = null;
	private String time = null;
	private Integer page = 1;
    private Integer numberResults = null;
    private Integer totalPages = null;
    private Integer nextPage = null;
    private Integer previousPage = null;
    
	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(Integer previousPage) {
		this.previousPage = previousPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getNumberResults() {
		return numberResults;
	}

	public void setNumberResults(Integer numberResults) {
		this.numberResults = numberResults;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<Llibre> getResults() {
		return results;
	}

	public void setResults(List<Llibre> results) {
		this.results = results;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String list()
	{
		long start = System.currentTimeMillis();
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		if (this.getPage() == null || this.getPage()<1) this.setPage(1);
		this.setNextPage(this.page+1);
		this.setPreviousPage(this.page-1);
		this.numberResults = com.vaannila.ws.BooksWS.numberResults(this.key);
		this.totalPages = this.numberResults/10;
		if((this.numberResults%10)!=0) this.totalPages++;
		if (this.getPage() > this.totalPages) this.setPage(this.totalPages);
		if (this.getNextPage() > this.totalPages) this.setNextPage(null);
		if (this.getPreviousPage() < 1) this.setPreviousPage(null);
		this.results = com.vaannila.ws.BooksWS.serchBook(this.key, Integer.toString(this.page));
		
		long end = System.currentTimeMillis();

		Double elapsed = (end-start)/1000.0;
		this.time = Double.toString(elapsed);
		this.msg = "No hi ha cap missatge";
		return SUCCESS;
	}
	
	public String show()
	{
		return SUCCESS;
	}
	
}
