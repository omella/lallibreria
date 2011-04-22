package com.vaannila.web;

import java.util.ArrayList;
import java.util.HashMap;

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
		System.out.println(">"+this.key+"<");
		HashMap<String,String> book = new HashMap<String,String>();
		book.put("titol", "El Angel Perdido");
		book.put("autor", "Javier Sierra");
		book.put("isbn", "0001");
		this.results.add(book);
		
		HashMap<String,String> book2 = new HashMap<String,String>();
		book2.put("titol", "Quijote");
		book2.put("autor", "Cervantes");
		book2.put("isbn", "0002");
		this.results.add(book2);
		this.msg = "Resultats trobats per la paraula clau \""+this.key+"\", en 0.00002 segons";
		return SUCCESS;
	}
	
	public String show()
	{
		return SUCCESS;
	}
	
}
