package com.vaannila.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.ParameterMap;
import org.apache.struts2.interceptor.SessionAware;
import org.hsqldb.lib.MD5;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;
import com.vaannila.dao.CupoDAO;
import com.vaannila.dao.CupoDAOImpl;
import com.vaannila.dao.LlibreriaDAO;
import com.vaannila.dao.LlibreriaDAOImpl;
import com.vaannila.dao.MailDAO;
import com.vaannila.dao.MailDAOImpl;
import com.vaannila.dao.PuntuacioDAO;
import com.vaannila.dao.PuntuacioDAOImpl;
import com.vaannila.domain.Comentari;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Puntuacio;
import com.vaannila.domain.Usuari;

public class LlibreriaAction extends ActionSupport implements ModelDriven<Llibreria>, SessionAware{
	private Map session = ActionContext.getContext().getSession();
	private static final long serialVersionUID = -6659925652584240539L;
	private Comentari comment = new Comentari();
	private ComentariDAO comentariDAO = new ComentariDAOImpl();
	private String text = null;
	public Comentari getComment() {
		return comment;
	}

	public void setComment(Comentari comment) {
		this.comment = comment;
	}
	private Puntuacio puntuacio = (Puntuacio) this.session.get("puntuacio");
	public Puntuacio getPuntuacio() {
		return puntuacio;
	}

	public void setPuntuacio(Puntuacio puntuacio) {
		this.puntuacio = puntuacio;
	}
	private PuntuacioDAO puntuacioDAO = new PuntuacioDAOImpl();
	public PuntuacioDAO getPuntuacioDAO() {
		return puntuacioDAO;
	}

	public void setPuntuacioDAO(PuntuacioDAO puntuacioDAO) {
		this.puntuacioDAO = puntuacioDAO;
	}
	private Double punts;
	public Double getPunts() {
		return punts;
	}

	public void setPunts(Double punts) {
		this.punts = punts;
	}
	private List<Llibreria> llibreriaList = new ArrayList<Llibreria>();
	private LlibreriaDAO llibreriaDAO = new LlibreriaDAOImpl();
	private CupoDAO cupoDAO = new CupoDAOImpl();
	private List <Cupo> llistaCupons = new ArrayList<Cupo>();
	private String[] llistaTematica = {"Ciencia","Generic","Drama"};
	private String tematica = null;
	private String valor = null;
//OJO: si se pone la parte comentada de siguiente linea, no se registran las librerias
	private Llibreria llibreria = new Llibreria();//(Llibreria)this.session.get("libreria");
	private int idCupo;
	private String codi = null;
	private String valid = null;
	private String distance;
	private ParameterMap<String,String>llistaDistance = (ParameterMap<String, String>) session.get("distancias");
	private String lib = null;
	private MailDAO maildao = new MailDAOImpl();
	private String idLlibMap = null;
	private String posLlib = null;
	private String posUser = null;
	private Boolean voted =  (Boolean) session.get("voted2");
	private Usuari logged = (Usuari) session.get("user");
	private List<Comentari> commentList = (List<Comentari>) this.session.get("commentList");
	public String getPosLlib() {
		return posLlib;
	}

	public void setPosLlib(String posLlib) {
		this.posLlib = posLlib;
	}

	public String getPosUser() {
		return posUser;
	}

	public void setPosUser(String posUser) {
		this.posUser = posUser;
	}	
	
	public Boolean getVoted() {
		return voted;
	}

	public void setVoted(Boolean voted) {
		this.voted = voted;
	}

	public String getIdLlibMap() {
		return idLlibMap;
	}

	public void setIdLlibMap(String idLlibMap) {
		this.idLlibMap = idLlibMap;
	}

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
//		llibreria.setPassword(MD5.encodeString(arg0, arg1));
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
		this.llistaDistance = (ParameterMap<String, String>) session.get("distancias");
		
		if(llistaDistance == null) {
			
			llistaDistance = new ParameterMap<String,String>();
		}
		this.llistaDistance.put(lib,distance);
		this.session.put("distancias", this.llistaDistance);
		
	}
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLib() {
		return lib;
	}

	public void setLib(String lib) {
		this.lib = lib;
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
	
	public String addMark(){
		//Si ja ha votat, acabem!
		if (voted != null && voted == true) return SUCCESS;
		this.puntuacio = puntuacioDAO.getPuntuacioIsbn(this.llibreria.getMail());
		
		//Comprovació errors
		if (punts < 1.0) punts = 1.0;
		if (punts > 5.0) punts = 5.0;
		
		if (this.puntuacio == null)
		{
			this.puntuacio = new Puntuacio();
			this.puntuacio.setIsbn(this.llibreria.getMail());
			this.puntuacio.setNumVots(1);
			this.puntuacio.setPuntuacio(this.punts);
		}
		else
		{
			puntuacio.setPuntuacio(((puntuacio.getPuntuacio()*puntuacio.getNumVots())+this.punts)/(puntuacio.getNumVots()+1));
			puntuacio.setNumVots(puntuacio.getNumVots()+1);
		}
		
		puntuacioDAO.savePuntuacio(puntuacio);
		this.puntuacio.setPuntuacio(unDecimal(this.puntuacio.getPuntuacio()));
		this.session.put("puntuacio", puntuacio);
		voted = true;
		session.put("voted2",voted);	
		this.posLlib = (String) this.session.get("posLlib");
		this.posUser = (String) this.session.get("posUser");
		return SUCCESS;
	}
	public String addComment(){
		
		Date data = new Date();		
		comment.setData(data);
	    String username = null;
	    Usuari user= (Usuari) session.get("user");
	    if (user == null) return ERROR;
	    username = user.getName();
	    if (username==null)username = "rodonako";
		comment.setUsername(username);
		comment.setIsbn(this.llibreria.getMail());
		comentariDAO.saveComentari(comment);
		this.commentList = comentariDAO.getComentariList(this.llibreria.getMail());
		this.session.put("commentList",this.commentList);
		this.setLlibreria(this.llibreria);
		this.posLlib = (String) this.session.get("posLlib");
		this.posUser = (String) this.session.get("posUser");
		
		return SUCCESS;
	}
	
	public String commentsAjax(){
		Date data = new Date();		
		comment.setData(data);
		comment.setText(this.text);
	    String username = null;
	    Usuari user= (Usuari) session.get("user");
	    username = user.getName();
	    if (username==null)username = "rodonako";
		comment.setUsername(username);
		comment.setIsbn(this.llibreria.getMail());
		comentariDAO.saveComentari(comment);
		this.commentList = comentariDAO.getComentariList(this.llibreria.getMail());
		this.session.put("commentList",this.commentList);
		this.posLlib = (String) this.session.get("posLlib");
		this.posUser = (String) this.session.get("posUser");
		//this.setLlibreria(this.llibreria);
		
		return SUCCESS;
	}	
	private Double unDecimal(Double x)
	{
		if (x!=null) {
			Double p = x*10;
			p = (double) Math.round(p);
			p = p/10;
			return p;
		}
		return null;
	}
	public String show(){
		
		if (this.llibreria != null) {
			if (!this.llibreria.getMail().equals(idLlibMap)) this.voted = false;
		}
		
		this.session.put("posLlib", this.posLlib);
		this.session.put("posUser", this.posUser);
		this.session.put("voted2", voted);
		this.llibreria = this.llibreriaDAO.getLlibreriaMail(idLlibMap);
		this.session.put("libreria", llibreria);
		this.commentList = comentariDAO.getComentariList(this.llibreria.getMail());
		this.session.put("commentList",this.commentList);
		this.puntuacio=puntuacioDAO.getPuntuacioIsbn(this.llibreria.getMail());
		this.puntuacio.setPuntuacio(unDecimal(this.puntuacio.getPuntuacio()));
		this.session.put("puntuacio", puntuacio);
		this.llistaCupons = this.cupoDAO.listCupoLlibreria(this.llibreria.getMail());
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

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public ParameterMap<String, String> getLlistaDistance() {
		return llistaDistance;
	}

	public void setLlistaDistance(ParameterMap<String, String> llistaDistance) {
		this.llistaDistance = llistaDistance;
	}

	public Usuari getLogged() {
		return logged;
	}

	public void setLogged(Usuari logged) {
		this.logged = logged;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Comentari> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comentari> commentList) {
		this.commentList = commentList;
	}

	

}
