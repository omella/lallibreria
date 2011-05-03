package com.vaannila.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;

import javax.servlet.http.*;

import com.googlecode.sslplugin.annotation.Secured;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.SessionAware;

import org.opensocial.*;
import org.opensocial.auth.*;
import org.opensocial.models.*;
import org.opensocial.providers.*;
import org.opensocial.services.*;

/**
 * <p> Validate a user login. </p>
 */
public  class LoginAction extends ActionSupport implements SessionAware, CookiesAware {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6637502602358242851L;
	static final Logger logger = Logger.getLogger(LoginAction.class);
	String error = null;
	Map<String, String> cookiesMap;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
 
	public String execute(){
		Provider provid = new FriendConnectProvider();
		//AuthScheme scheme2 = new OAuth2LeggedScheme("*:06834717057300479661", "cabs_uiKs-E=");
		
		
		String siteId = "06834717057300479661";
		String token = null;
		 Map session = ActionContext.getContext().getSession();
		 if (cookiesMap.containsKey("fcauth" + siteId))
		 {
			 token = cookiesMap.get("fcauth06834717057300479661");
		 }
		if (token!=null){
			AuthScheme scheme = new FCAuthScheme(token);
			
			Client client = new Client(provid, scheme);
			
			Request request = PeopleService.getViewer();
			Response response;
			try {
				response = client.send(request);
				Person viewer = response.getEntry();
				
				this.username=viewer.getDisplayName();
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		if(this.username == null){
            this.error = "Has possat la pota! Mira aqui dalt i torna-ho a intentar!";
            return ERROR;
	    }
		
	    else{
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
	public void setCookiesMap(Map arg0) {
		this.cookiesMap = arg0;
		
	}

}
