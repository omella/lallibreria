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
	private String tematica = null;
	private String valor = null;
	private Map session = ActionContext.getContext().getSession();
	private Llibreria llibreria = (Llibreria)this.session.get("libreria");

	@Override
	public Llibreria getModel() {
		return llibreria;
	}
	
	public String add()
	{
		llibreriaDAO.saveLlibreria(llibreria);
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
			return SUCCESS;
		}
		else return e;		
	}
	
	public String addCupo()
	{
		Cupo cupo = new Cupo();
		cupo.setLlibreria("prova");
		cupo.setTematica(this.tematica);
		cupo.setValor(Double.valueOf(this.valor));
		cupoDAO.saveCupo(cupo);
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
