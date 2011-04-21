package com.vaannila.web;

import java.util.ArrayList;
import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.ModelDriven;
//import com.vaannila.dao.searchDAO;
//import com.vaannila.dao.searchDAOImpl;
//import com.vaannila.domain.Cerca;




//public class SearchAction extends ActionSupport implements ModelDriven<Cerca>{
public class SearchAction extends ActionSupport {

	private static final long serialVersionUID = -4978596336944380865L;
	//private Cerca cerca = new Cerca();
	//private ArrayList<HashMap<String,String> > results = new ArrayList<HashMap<String,String> >();
	//private searchDAO searchDAO = new searchDAOImpl();
	private String msg = null;
	private String key = null;
	


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
		//results=obtenirResultats(key)
		this.msg = "Estas buscant \""+this.getKey()+"\", ens sap greu, pero aquesta funcio no esta implementada... :-(";
		return SUCCESS;
	}
	
	public String show()
	{
		return SUCCESS;
	}
	
}
