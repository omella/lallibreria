package com.vaannila.web;

import java.io.IOException;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.restfb.*;
import com.restfb.types.User;
import com.vaannila.dao.UserDAO;
import com.vaannila.dao.UserDAOImpl;
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
	private boolean google = false;
    private String username = "";
    private String usermail = "";
	private String serviceId = "";
	private String GsiteId = "06834717057300479661";
	private String FsiteId = "177068509007802";
	private String token = null;
	private Usuari usuari = new Usuari();
	private String gender = "";
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
			    google = true;
			    break;
	    	}	    	
	    	if (token == null && c.getName().equals("fbs_" + FsiteId)) {
			    token = c.getValue().replaceAll("%7C", "|").substring(13,112);
			    google = false;
				break;
	    	}
	    }
	    
		if (token!=null && google){	
			return INPUT;
		}
		else if (token!=null && !google){
			
			facebookClient = new DefaultFacebookClient(token);  
			User user = facebookClient.fetchObject("me", User.class);
			
			serviceId = user.getId();
			username=user.getName();
			gender = user.getGender();
			usermail = user.getEmail();

			usuari.setName(username);
			usuari.setIsGoogleAccount(google);
			usuari.setServiceId(serviceId);
			usuari.setGender(gender);
			usuari.setMail(usermail);

			boolean b = userdao.existUser(serviceId, google);
			if(!b){
				userdao.saveUser(usuari);
			}
			else{
				usuari=userdao.getUser(usuari.getServiceId(), usuari.getIsGoogleAccount());
			}
			session.put("user", usuari);

			return SUCCESS;
		}
		else{
            this.error = "Has possat la pota! Mira aqui dalt i torna-ho a intentar!";
            return ERROR;
	    }
	    	
  }
	
	public String segonaPart(){
		GsiteId = "06834717057300479661";
	    for(Cookie c : servletRequest.getCookies()) {
	    	if (token == null && c.getName().equals("fcauth" + GsiteId)) {
			    token = c.getValue();
			    google = true;
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
			
			serviceId = viewer.getId();
			username = viewer.getDisplayName();
			
			usuari.setName(username);
			usuari.setIsGoogleAccount(google);
			usuari.setServiceId(serviceId);
			usuari.setGender(gender);
			usuari.setMail(usermail);
			
			boolean b = userdao.existUser(serviceId, google);
			if(!b){
				userdao.saveUser(usuari);
			}
			else{
				usuari=userdao.getUser(usuari.getServiceId(), usuari.getIsGoogleAccount());
			}
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


}
