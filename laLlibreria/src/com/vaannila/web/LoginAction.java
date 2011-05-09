package com.vaannila.web;

import java.io.IOException;
import java.util.List;
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
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
 
	public String execute(){
		Provider Gprovid = new FriendConnectProvider();
		//AuthScheme scheme2 = new OAuth2LeggedScheme("*:06834717057300479661", "cabs_uiKs-E=");
		
		boolean google = false;
		String id = "";
		String GsiteId = "06834717057300479661";
		String FsiteId = "177068509007802";
		String token = null;
		Usuari usuari = new Usuari();
		Map session = ActionContext.getContext().getSession();

	    for(Cookie c : servletRequest.getCookies()) {
	    	if (c.getName().equals("fcauth" + GsiteId)) {
			    token = c.getValue();
			    google = true;
			    break;
	    	}
	    	if (c.getName().equals("fbs_" + FsiteId)) {
			    token = c.getValue();
				System.out.println("EL meu token ("+token+")");
			    break;
	    	}
	    }
	    
		if (token!=null && google){
			AuthScheme scheme = new FCAuthScheme(token);
			
			Client Gclient = new Client(Gprovid, scheme);
			
			Request request = PeopleService.getViewer();
			Response response;
			try {
				response = Gclient.send(request);
				Person viewer = response.getEntry();
				
				this.username=viewer.getDisplayName();
				id = viewer.getId();
				
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		if (token!=null && !google){
			token = token.replaceAll("%7C", "|").substring(13,112);
			facebookClient = new DefaultFacebookClient(token);  
			User user = facebookClient.fetchObject("me", User.class);
			
			this.username=user.getName();
			id = user.getId();
			
		}
		if(this.username == null){
            this.error = "Has possat la pota! Mira aqui dalt i torna-ho a intentar!";
            return ERROR;
	    }
		
	    else{
			usuari.setName(username);
			usuari.setIsGoogleAccount(google);
			usuari.setId(Long.valueOf(id));
			usuari.setAboutYou("a");
			usuari.setCountry("a");
			usuari.setGender("a");
			usuari.setMailingList(true);
			usuari.setPassword("a");
			
			UserAction a = new UserAction();
			a.list();
			List<Usuari> userList = a.getUserList();
			boolean b = false;
			for(int i = 0; userList != null && !b && i < userList.size();++i){
				b = (userList.get(i).getId() == usuari.getId() && userList.get(i).getIsGoogleAccount() == usuari.getIsGoogleAccount());
			}
			if(!b){
				a.setUser(usuari);
				a.add();
			}
			session.put("username", this.username);
	    	//session.remove("sessionLoginFail");
	    	return SUCCESS;
	    }
		/*
		DOMConfigurator.configure("/laLlibreria/workspace/laLlibreria/src/log4j.xml");
		logger.debug("Sample debug message");
		logger.info("Sample info message");
		logger.warn("Sample warn message");
		logger.error("Sample error message");
		logger.fatal("Sample fatal message");
        System.out.println("Validating login");

	    */
  }


    // ---- Username property ----

    /**
     * <p>Field to store User username.</p>
     * <p/>
     */
    private String username = null;


    /**
     * <p>Provide User username.</p>
     *
     * @return Returns the User username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Store new User username</p>
     *
     * @param value The username to set.
     */
    public void setUsername(String value) {
        username = value;
    }

    // ---- Username property ----

    /**
     * <p>Field to store User password.</p>
     * <p/>
     */
    private String password = null;


    /**
     * <p>Provide User password.</p>
     *
     * @return Returns the User password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Store new User password</p>
     *
     * @param value The password to set.
     */
    public void setPassword(String value) {
        password = value;
    }

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
	    this.servletRequest = servletRequest;
	}


}
