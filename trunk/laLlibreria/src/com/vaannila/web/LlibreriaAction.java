package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.domain.Llibreria;

public class LlibreriaAction extends ActionSupport implements ModelDriven<Llibreria> {

	private static final long serialVersionUID = -6659925652584240539L;

	private Llibreria llibreria = new Llibreria();
	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	
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

}
