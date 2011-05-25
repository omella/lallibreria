package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
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

public class HomeAction extends ActionSupport implements ModelDriven<Vist>, SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -297863529510813730L;

	private Vist viewed = new Vist();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private VistDAO vistDAO = new VistDAOImpl();
	private Map session = ActionContext.getContext().getSession();

	List<Llibre> populars = null;
	List<Cupo> millorOfertes = null;
	private Boolean loguejat = (Boolean) session.get("loguejat");
	private Boolean loginwithGoogle=(Boolean) session.get("loginwithGoogle");
	private String token=(String) session.get("token");
	private String posicio = (String) session.get("posicio");
	public String getToken() {
		token=null;
		session.put("token",token);
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getLoginwithGoogle() {
		if (loginwithGoogle == null) loginwithGoogle = false;
		session.put("loginwithGoogle", loginwithGoogle);
		return loginwithGoogle;
	}

	public void setLoginwithGoogle(Boolean loginwithGoogle) {
		this.loginwithGoogle = loginwithGoogle;
	}

	public Boolean getLoguejat() {
		if (loguejat == null) loguejat = false;
		session.put("loguejat", loguejat);
		return loguejat;
	}

	public void setLoguejat(Boolean loguejat) {
		this.loguejat = loguejat;
	}
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
		this.session.put("llibreriaList", this.llibreriaList);
		List<Cupo> llistaOfertes = getMillorsOfertes();
		this.setMillorOfertes(llistaOfertes);
		this.setPopulars(llistaLlibres); 
		this.session.put("populars", populars);
		this.session.put("millorOfertes", millorOfertes);
		if (loguejat == null) loguejat = false;
		if (loginwithGoogle == null) loginwithGoogle = false;		
		this.session.put("loguejat", loguejat);
		this.session.put("loginwithGoogle", loginwithGoogle);

		return SUCCESS;
	}

	private List<Cupo> getMillorsN_Ofertes(int n, List<Cupo> mill){
		List<Cupo> mill_N = new ArrayList<Cupo>();
		for (int i = 0; i < n; ++i) {
			if(mill.get(i) != null) mill_N.add(mill.get(i));
		}
		return mill_N;
	}
	
	private List<Cupo> getMillorsOfertes() {
		List<Cupo> millors = this.llibreriaDAO.getCuponsLlibreria();
		if (millors != null){
			int n = 6;
			if (millors.size() < n) n = millors.size();
				
			millors = getMillorsN_Ofertes(n,millors);
			millors = cupoMailToNom(millors);
			return millors;
		}
		return null;
	}
	
	private List<Cupo> cupoMailToNom(List<Cupo> lla) {
		List<Cupo> result = new ArrayList<Cupo>();
		int n = lla.size();
		for (int i = 0; i < n; ++i) {
			Cupo local_cupo = lla.get(i);
			String nameLlib = lla.get(i).getLlibreria_cupo();
			Llibreria llibLocal = this.llibreriaDAO.getLlibreriaMail(nameLlib);
			local_cupo.setLlibreria_name(llibLocal.getName());
			result.add(local_cupo);
		}
		return result;
	}

	@Override
	public Vist getModel() {
		// TODO Auto-generated method stub
		return viewed;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session=arg0;
		
	}
	
	public void updatePosicio()
	{
		session.put("posicio", this.posicio);
	}

	public String getPosicio() {
		return posicio;
	}

	public void setPosicio(String posicio) {
		this.posicio = posicio;
	}
	
	
}
