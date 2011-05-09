package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibreria;

public class LlibreriaAction extends ActionSupport implements ModelDriven<Llibreria> {

	private static final long serialVersionUID = -6659925652584240539L;

	private Llibreria llibreria = new Llibreria();
	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private String tematica = null;
	private String valor = null;
	//borrame
	private String mail = null;
	private String password = null;
	//FIN-borrame
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
		if (llibreriaDAO.existLlibreria(mail, password)) return SUCCESS;
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

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMail() {
		return mail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}


	

}
