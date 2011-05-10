package com.vaannila.web;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.PuntuacioDAO;
import com.vaannila.dao.PuntuacioDAOImpl;
import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.Comentari;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Puntuacio;
import com.vaannila.domain.Vist;



public class HomeLlibreriaAction extends ActionSupport implements ModelDriven<Cupo>{

	private static final long serialVersionUID = -9113041734859241965L;
	Cupo nouCupo = new Cupo();
	private List <Cupo> llistaCupons = new ArrayList<Cupo>();
    CupoDAO cupoDAO = new CupoDAOImpl();

	
	public List<Cupo> getLlistaCupons() {
		return llistaCupons;
	}


	public void setLlistaCupons(List<Cupo> llistaCupons) {
		this.llistaCupons = llistaCupons;
	
	}

	public String list()
	{
		//llistaCupons = cupoDAO.listCupoLlibreria(0);
		return SUCCESS;
	}

	public String add() {
		Map session = ActionContext.getContext().getSession();
		String nomLlibreria = (String) session.get("username");
		
		this.nouCupo.setLlibreria("PonNomLlibreria");
		cupoDAO.saveCupo(this.nouCupo);
		//llistaCupons = cupoDAO.listCupoLlibreria(0);
		return SUCCESS;
	}
	
	@Override
	public Cupo getModel() {
		return this.nouCupo;
	}



}
