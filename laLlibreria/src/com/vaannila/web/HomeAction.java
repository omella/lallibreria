package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.vaannila.dao.VistDAO;
import com.vaannila.dao.VistDAOImpl;

import com.vaannila.domain.Llibreria;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.QueryPair;
import com.vaannila.domain.Vist;

public class HomeAction extends ActionSupport implements ModelDriven<Vist>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -297863529510813730L;

	private Vist viewed = new Vist();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	
	List<Llibre> populars = null;
	List<Cupo> millorOfertes = null;
	
	public List<Cupo> getMillorOfertes() {
		return millorOfertes;
	}

	public void setMillorOfertes(List<Cupo> millorOfertes) {
		this.millorOfertes = millorOfertes;
	}

	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	
	public List<Llibreria> getLlibreriaList() {
		return llibreriaList;
	}

	public void setLlibreriaList(List<Llibreria> llibreriaList) {
		this.llibreriaList = llibreriaList;
	}

	public List<Llibre> getPopulars() {
		return populars;
	}

	public void setPopulars(List<Llibre> populars) {
		this.populars = populars;
	}

	public String home() {
		
		List<QueryPair> vistes = vistDAO.getVistList();
		List<Llibre> llistaLlibres = new ArrayList<Llibre>();
		for (int i=0; i<vistes.size(); i++) {
			llistaLlibres.add(com.vaannila.ws.BooksWS.getBook(vistes.get(i).ISBN));
		}
		this.setLlibreriaList(llibreriaDAO.listLlibreria());
		List<Cupo> llistaOfertes = getMillorsOfertes();
		this.setMillorOfertes(llistaOfertes);
		this.setPopulars(llistaLlibres);   

		return SUCCESS;
	}

	private List<Cupo> getMillorsOfertes() {
		List<Cupo> lo = this.llibreriaDAO.getCuponsLlibreria();
		List<Cupo> millors = new ArrayList<Cupo>();
		int n = lo.size();
		millors.add(lo.get(0));
		for (int i = 1; i < n; ++i){
			int m = millors.size();
			for (int j = 0; j < m; ++j) {
				if (millors.get(j).getValor() > lo.get(i).getValor()){
					millors.remove(j);
					millors.add(lo.get(i));
					millors.add(millors.get(j));
				}
			}
		}
		millors = cupoMailToNom(millors);
		return millors;
	}
	
	private List<Cupo> cupoMailToNom(List<Cupo> lla) {
		List<Cupo> result = new ArrayList<Cupo>();
		int n = lla.size();
		for (int i = 0; i < n; ++i) {
			Cupo trans = lla.get(i);
			String nameLlib = lla.get(i).getLlibreria_cupo();
			Llibreria llibLocal = this.llibreriaDAO.getLlibreriaMail("davadc88@gmail.com");
			trans.setLlibreria_cupo(llibLocal.getName());
			result.add(trans);
		}
		return result;
	}

	@Override
	public Vist getModel() {
		// TODO Auto-generated method stub
		return viewed;
	}
}
