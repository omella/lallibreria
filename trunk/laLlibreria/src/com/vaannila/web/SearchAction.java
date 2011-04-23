package com.vaannila.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.ModelDriven;
//import com.vaannila.dao.searchDAO;
//import com.vaannila.dao.searchDAOImpl;
//import com.vaannila.domain.Cerca;




//public class SearchAction extends ActionSupport implements ModelDriven<Cerca>{
public class SearchAction extends ActionSupport {

	private static final long serialVersionUID = -4978596336944380865L;
	//private Cerca cerca = new Cerca();
	
	//private searchDAO searchDAO = new searchDAOImpl();
	private ArrayList<HashMap<String,String> > results = new ArrayList<HashMap<String,String> >();
	private String msg = null;
	private String key = null;
	private Integer page = null;
    private Integer numberResults = null;
    private Integer totalPages = null;
    
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

	public ArrayList<HashMap<String, String>> getResults() {
		return results;
	}

	public void setResults(ArrayList<HashMap<String, String>> results) {
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


	public String list()
	{
		long start = System.currentTimeMillis();
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		this.setPage(1);
		this.numberResults = com.vaannila.ws.ISBNdbWS.numberResults(this.key);
		this.totalPages = this.numberResults/10;
		if((this.numberResults%10)!=0) this.totalPages++;
		this.results = com.vaannila.ws.ISBNdbWS.search(this.key, Integer.toString(this.page));
		Map session = ActionContext.getContext().getSession();
	    session.put("searchPage", this.page);
	    
		//HashMap<String,String> book = new HashMap<String,String>();
		//book.put("titol", "El Angel Perdido");
		//book.put("autor", "Javier Sierra");
		//book.put("isbn", "0001");
		//this.results.add(book);
		
		//HashMap<String,String> book2 = new HashMap<String,String>();
		//book2.put("titol", "Quijote");
		//book2.put("autor", "Cervantes");
		//book2.put("isbn", "0002");
		//this.results.add(book2);
		
		long end = System.currentTimeMillis();

		Double elapsed = (end-start)/1000.0;
		
		this.msg = " resultats trobats per la paraula clau \""+this.key+"\", en "+Double.toString(elapsed)+" segons";
		return SUCCESS;
	}
	
	public String next()
	{
		long start = System.currentTimeMillis();
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		Map session = ActionContext.getContext().getSession();
		this.setPage((Integer) session.get("searchPage"));
		this.page++;
		session.put("searchPage", this.page);
		this.numberResults = com.vaannila.ws.ISBNdbWS.numberResults(this.key);
		this.totalPages = this.numberResults/10;
		if((this.numberResults%10)!=0) this.totalPages++;
		this.results = com.vaannila.ws.ISBNdbWS.search(this.key, Integer.toString(this.page));
		
		long end = System.currentTimeMillis();

		Double elapsed = (end-start)/1000.0;
		
		this.msg = " resultats trobats per la paraula clau \""+this.key+"\", en "+Double.toString(elapsed)+" segons";
		return SUCCESS;
	}
	
	public String previous()
	{
		long start = System.currentTimeMillis();
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		Map session = ActionContext.getContext().getSession();
		this.setPage((Integer) session.get("searchPage"));
		this.page--;
		session.put("searchPage", this.page);
		this.numberResults = com.vaannila.ws.ISBNdbWS.numberResults(this.key);
		this.totalPages = this.numberResults/10;
		if((this.numberResults%10)!=0) this.totalPages++;
		this.results = com.vaannila.ws.ISBNdbWS.search(this.key, Integer.toString(this.page));
		
		long end = System.currentTimeMillis();

		Double elapsed = (end-start)/1000.0;
		
		this.msg = " resultats trobats per la paraula clau \""+this.key+"\", en "+Double.toString(elapsed)+" segons";
		return SUCCESS;
	}
	
	public String show()
	{
		return SUCCESS;
	}
	
}
