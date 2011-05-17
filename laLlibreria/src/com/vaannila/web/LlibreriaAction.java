package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;

public class LlibreriaAction extends ActionSupport implements ModelDriven<Llibreria>, SessionAware{

	private static final long serialVersionUID = -6659925652584240539L;

	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private List <Cupo> llistaCupons = new ArrayList<Cupo>();
	private String[] llistaTematica = {"Ciencia","Generic","Drama"};
	private String tematica = null;
	private String valor = null;
	private Map session = ActionContext.getContext().getSession();
	private Llibreria llibreria = new Llibreria();//(Llibreria)this.session.get("libreria");
	private int idCupo;
	private String codi = null;
	private Boolean valid = null;
	private String distance;
	private ParameterMap<String,String>llistaDistance = (ParameterMap<String, String>) session.get("distancias");

	private MailDAO maildao = new MailDAOImpl();
	public int getIdCupo() {
		return idCupo;
	}

	public void setIdCupo(int idCupo) {
		this.idCupo = idCupo;
	}

	@Override
	public Llibreria getModel() {
		return llibreria;
	}
	
	public String add()
	{
		
		llibreriaDAO.saveLlibreria(llibreria);
	
		this.session.put("libreria", llibreria);
		//list();	
		return SUCCESS;
	}
	
	public String list()
	{
		
		llibreriaList = llibreriaDAO.listLlibreria();
		this.session.put("llibreriaList", llibreriaList);
		return SUCCESS;
	}
	
	public String listLlibreria()
	{
		llibreriaList = llibreriaDAO.listLlibreria();
		for (int i = 0; i < llibreriaList.size();++i){
			if(llibreriaList.get(i).getName().equals(llibreria.getName())){
				llibreria=llibreriaList.get(i);
				llistaCupons=cupoDAO.listCupoLlibreria(llibreria.getMail());
				break;
			}
		}
		this.session.put("llibreria", llibreria);
		this.session.put("llistaCupons",llistaCupons);
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
		cupo.setLlibreria_cupo(llib_local.getMail());
		cupo.setTematica(this.tematica);
		cupo.setValor(Double.valueOf(this.valor));
		cupoDAO.saveCupo(cupo);
		llistaCupons = cupoDAO.listCupoLlibreria(llib_local.getMail());
		session.put("llistaCupons", llistaCupons);
		return SUCCESS;
	}
	public void novaDistance()
	{
		this.llibreria = (Llibreria) session.get("libreria");
		if(llistaDistance == null) {
			llistaDistance = new ParameterMap<String,String>();
		}
		this.llistaDistance.put(this.llibreria.getName(),this.distance);
		this.session.put("distancias", this.llistaDistance);
		
	}
	public String eliminaCupo() {
		this.llistaCupons=(List<Cupo>) session.get("llistaCupons");
		for(int i = 0; i < llistaCupons.size(); ++i) {
			if(llistaCupons.get(i).getId() == idCupo){
				Cupo cupo = llistaCupons.get(i);				
				cupoDAO.deleteCupo(cupo);
				llistaCupons.remove(cupo);				
			}
		}
		return SUCCESS;
	}
	
	public String check() 
	{
		this.llibreria = (Llibreria) session.get("libreria");
		this.valid = maildao.checkCodi(this.codi,this.llibreria.getMail());
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
		llistaCupons = cupoDAO.listCupoLlibreria(llib_local.getMail());
		session.put("llistaCupons", llistaCupons);
		return SUCCESS;
	}
	
	public String show(){
		this.llibreria = (Llibreria)this.session.get("libreria");
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

	public void setLlistaTematica(String[] llistaTematica) {
		this.llistaTematica = llistaTematica;
	}

	public String[] getLlistaTematica() {
		return llistaTematica;
	}

	public String getCodi() {
		return codi;
	}

	public void setCodi(String codi) {
		this.codi = codi;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	

}
