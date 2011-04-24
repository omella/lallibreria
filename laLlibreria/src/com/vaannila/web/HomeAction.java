package com.vaannila.web;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.QueryPair;
import com.vaannila.domain.Vist;

public class HomeAction extends ActionSupport implements ModelDriven<Vist>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -297863529510813730L;

	private Vist viewed = new Vist();
	
	private VistDAO vistDAO = new VistDAOImpl();
	
	List<QueryPair> populars = null;
	
	public List<QueryPair> getPopulars() {
		return populars;
	}

	public void setPopulars(List<QueryPair> populars) {
		this.populars = populars;
	}

	public String home() {
		
		List<QueryPair> vistes = vistDAO.getVistList();
		this.setPopulars(vistes);

		return SUCCESS;
	}

	@Override
	public Vist getModel() {
		// TODO Auto-generated method stub
		return viewed;
	}
}
