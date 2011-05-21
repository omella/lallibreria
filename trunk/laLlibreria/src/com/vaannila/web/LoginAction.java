package com.vaannila.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.restfb.*;
import com.restfb.types.User;
import com.vaannila.dao.UserDAO;
import com.vaannila.dao.UserDAOImpl;
import com.vaannila.domain.Cupo;
import com.vaannila.domain.Llibre;
import com.vaannila.domain.Llibreria;
import com.vaannila.domain.Usuari;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import org.opensocial.*;
import org.opensocial.auth.*;
import org.opensocial.models.*;
import org.opensocial.providers.*;
import org.opensocial.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

/**
 * <p> Validate a user login. </p>
 */
public  class LoginAction extends ActionSupport implements SessionAware, ServletRequestAware {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6637502602358242851L;
	static final Logger logger = Logger.getLogger(LoginAction.class);
	String error = null;
	protected HttpServletRequest servletRequest;
	private FacebookClient facebookClient;
	private Map session = ActionContext.getContext().getSession();
	private UserDAO userdao = new UserDAOImpl();
	private String google = "false";
    private String username = "";
    private String usermail = "";
	private String serviceId = "";
	private String GsiteId = "06834717057300479661";
	private String FsiteId = "177068509007802";
	private String token = null;
	private Usuari usuari = new Usuari();
	private String gender = "";
	private Boolean loguejat = (Boolean) session.get("loguejat");
	private Boolean loginwithGoogle=(Boolean) session.get("loginwithGoogle");
	private String errorFormulari = null;
	private List<Llibreria> llibreriaList = (List<Llibreria>) session.get("llibreriaList");
	private List<Llibre> populars = (List<Llibre>) session.get("populars");;
	private List<Cupo> millorOfertes = (List<Cupo>) session.get("millorOfertes");

	public String getErrorFormulari() {
		return errorFormulari;
	}

	public void setErrorFormulari(String errorFormulari) {
		this.errorFormulari = errorFormulari;
	}

	public List<Cupo> getMillorOfertes() {
		return millorOfertes;
	}

	public void setMillorOfertes(List<Cupo> millorOfertes) {
		this.millorOfertes = millorOfertes;
	}
	
	public List<Llibre> getPopulars() {
		return populars;
	}

	public void setPopulars(List<Llibre> populars) {
		this.populars = populars;
	}
	
	public Boolean getLoginwithGoogle() {
		session.put("loginwithGoogle", loginwithGoogle);
		return loginwithGoogle;
	}

	public void setLoginwithGoogle(Boolean loginwithGoogle) {
		this.loginwithGoogle = loginwithGoogle;
	}

	public Boolean getLoguejat() {
		session.put("loguejat", loguejat);
		return loguejat;
	}

	public void setLoguejat(Boolean loguejat) {
		this.loguejat = loguejat;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
 
	public String primeraPart(){
		
	    for(Cookie c : servletRequest.getCookies()) {
	    	if (token == null && c.getName().equals("fcauth" + GsiteId)) {
			    token = c.getValue();
			    google = "true";
			    break;
	    	}	    	
	    	if (token == null && c.getName().equals("fbs_" + FsiteId)) {
	    		System.out.println(c.getValue().replaceAll("%7C", "|"));
			    token = c.getValue().replaceAll("%7C", "|").substring(13,104);
	    		System.out.println(token);
			    google = "false";
				break;
	    	}
	    }
	    
		if (token!=null && google=="true"){	
			Provider Gprovid = new FriendConnectProvider();
			AuthScheme scheme = new FCAuthScheme(token);
			
			Client Gclient = new Client(Gprovid, scheme);
			
			Request request = PeopleService.getViewer();
			Response response;
			try {
				response = Gclient.send(request);
				Person viewer = response.getEntry();
				
				serviceId = viewer.getId().substring(5);
				
				boolean b = userdao.existUser(serviceId, google);
				if(!b){
					return INPUT;
				}
				else{
					usuari=userdao.getUser(serviceId, google);
				}
				loguejat=true;
				loginwithGoogle = true;
				session.put("loginwithGoogle", loginwithGoogle);
				session.put("loguejat", loguejat);
				session.put("user", usuari);
				
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return SUCCESS;
		}
		else if (token!=null && google=="false"){
			
			facebookClient = new DefaultFacebookClient(token);  
			User user = facebookClient.fetchObject("me", User.class);
			
			serviceId = user.getId();


			boolean b = userdao.existUser(serviceId, google);
			if(!b){
				username=user.getName();
				gender = user.getGender();
				usermail = user.getEmail();
				usuari.setName(username);
				usuari.setIsGoogleAccount(google);
				usuari.setServiceId(serviceId);
				usuari.setGender(gender);
				usuari.setMail(usermail);
				userdao.saveUser(usuari);
			}
			else{
				usuari=userdao.getUser(serviceId, google);
			}
			loguejat=true;
			loginwithGoogle = false;
			session.put("loginwithGoogle", loginwithGoogle);
			session.put("loguejat", loguejat);
			session.put("user", usuari);

			return SUCCESS;
		}
		else{
            this.error = "Has possat la pota! Mira aqui dalt i torna-ho a intentar!";
            return ERROR;
	    }
	    	
  }
    public boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }
    }

	public String segonaPart(){
		
		if(!isEmail(usermail)){
			this.setErrorFormulari("El email introduit no és correcte!");
			return INPUT;
		}
		
		GsiteId = "06834717057300479661";
	    for(Cookie c : servletRequest.getCookies()) {
	    	if (token == null && c.getName().equals("fcauth" + GsiteId)) {
			    token = c.getValue();
			    google = "true";
			    break;
	    	}	
	    }
		Provider Gprovid = new FriendConnectProvider();
		AuthScheme scheme = new FCAuthScheme(token);
		
		Client Gclient = new Client(Gprovid, scheme);
		
		Request request = PeopleService.getViewer();
		Response response;
		try {
			response = Gclient.send(request);
			Person viewer = response.getEntry();
			
			serviceId = viewer.getId().substring(5);
			username = viewer.getDisplayName();
			
			usuari.setName(username);
			usuari.setIsGoogleAccount(google);
			usuari.setServiceId(serviceId);
			usuari.setGender("noNecessari");
			usuari.setMail(usermail);
			
			userdao.saveUser(usuari);
			loguejat=true;
			loginwithGoogle = true;
			session.put("loginwithGoogle", loginwithGoogle);
			session.put("loguejat", loguejat);
			session.put("user", usuari);
			
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	public String logout(){
		google = "";
	    username = "";
	    usermail = "";
		serviceId = "";
		token = "";
		gender = "";
		loguejat=false;
		loginwithGoogle = false;
		session.put("loginwithGoogle", loginwithGoogle);
		session.put("loguejat", loguejat);
		session.remove("user");

		return SUCCESS;
	}
	
	
    public String getUsername() {
        return username;
    }

    /**
     * <p>Store new User username</p>
     *
     * @param value The username to set.
     */


    // ---- Username property ----

    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
	    this.servletRequest = servletRequest;
	}

	public void setLlibreriaList(List<Llibreria> llibreriaList) {
		this.llibreriaList = llibreriaList;
	}

	public List<Llibreria> getLlibreriaList() {
		return llibreriaList;
	}


}
