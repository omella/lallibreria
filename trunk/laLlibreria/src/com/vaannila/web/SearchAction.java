package com.vaannila.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.ModelDriven;
//import com.vaannila.dao.searchDAO;
//import com.vaannila.dao.searchDAOImpl;
//import com.vaannila.domain.Cerca;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibre;
import org.apache.struts2.interceptor.SessionAware;




//public class SearchAction extends ActionSupport implements ModelDriven<Cerca>{
public class SearchAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = -4978596336944380865L;
	//private Cerca cerca = new Cerca();
	
	//private searchDAO searchDAO = new searchDAOImpl();
	private List<Llibre> results = new ArrayList<Llibre>();
	private String msg = null;
	private String key = null;
	private String keyword = null;
	private String time = null;
	private Integer page = 1;
    private Integer numberResults = null;
    private Integer totalPages = null;
    private Integer nextPage = null;
    private Integer previousPage = null;
    private Map session = ActionContext.getContext().getSession();
    
	private List<Llibre> populars = (List<Llibre>) session.get("populars");;
	private List<Cupo> millorOfertes = (List<Cupo>) session.get("millorOfertes");
	
	public List<Cupo> getMillorOfertes() {
		return millorOfertes;
	}

	public void setMillorOfertes(List<Cupo> millorOfertes) {
		this.millorOfertes = millorOfertes;
	}
	
	public List<Llibre> getPopulars() {
		return populars;
	}

	public void setPopulars(List<Llibre> populars) {
		this.populars = populars;
	}
    
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
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
		if(this.key==null || this.key.equals("") || this.key.equals("")) return ERROR;
		long start = System.currentTimeMillis();
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		if (this.getPage() == null || this.getPage()<1) this.setPage(1);
		this.setNextPage(this.page+1);
		this.setPreviousPage(this.page-1);
		this.setKeyword(this.getKey().replaceAll(" ", "+"));
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
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}
}
