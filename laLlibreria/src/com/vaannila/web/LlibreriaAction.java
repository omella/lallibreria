package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;

public class LlibreriaAction extends ActionSupport implements ModelDriven<Llibreria>, SessionAware{

	private static final long serialVersionUID = -6659925652584240539L;

	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private List <Cupo> llistaCupons = new ArrayList<Cupo>();
	private String tematica = null;
	private String valor = null;
	private Map session = ActionContext.getContext().getSession();
	private Llibreria llibreria = new Llibreria();//(Llibreria)this.session.get("libreria");

	@Override
	public Llibreria getModel() {
		return llibreria;
	}
	
	public String add()
	{
		
		llibreriaDAO.saveLlibreria(llibreria);
		this.session.put("libreria", llibreria);
		list();	
		return SUCCESS;
	}
	
	public String list()
	{
		
		llibreriaList = llibreriaDAO.listLlibreria();
		return SUCCESS;
	}

	public String login(){
		String e = "error";	
		if (llibreriaDAO.existLlibreria(llibreria.getMail(), llibreria.getPassword())){	
			this.session.put("libreria", llibreria);
			listCupons();
			return SUCCESS;
		}
		else return e;		
	}
	
	public String addCupo()
	{
		Cupo cupo = new Cupo();
		Llibreria llib_local = (Llibreria)this.session.get("libreria");
		cupo.setLlibreria(llib_local.getMail());
		cupo.setTematica(this.tematica);
		cupo.setValor(Double.valueOf(this.valor));
		cupoDAO.saveCupo(cupo);
		llistaCupons = cupoDAO.listCupoLlibreria("mrodon536@gmail.com");
		for(int i= 0; i < llistaCupons.size();++i) System.out.println("CUPON:"+llistaCupons.get(i).getLlibreria());
		return SUCCESS;
	}
	
	public Llibreria getLlibreria() {
		return llibreria;
	}

	public void setLlibreria(Llibreria llibreria) {
		this.llibreria = llibreria;
	}

	public List<Llibreria> getLlibreriaList() {
		return llibreriaList;
	}

	public void setLlibreriaList(List<Llibreria> llibreriaList) {
		this.llibreriaList = llibreriaList;
	}
	
	public List<Cupo> getLlistaCupons() {
		return llistaCupons;
	}


	public void setLlistaCupons(List<Cupo> llistaCupons) {
		this.llistaCupons = llistaCupons;
	
	}

	public String listCupons()
	{
		Llibreria llib_local = (Llibreria)this.session.get("libreria");
		llistaCupons = cupoDAO.listCupoLlibreria("mrodon536@gmail.com");
		return SUCCESS;
	}
	
	public String show(){
		return SUCCESS;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}


}
