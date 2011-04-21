package com.vaannila.web;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * <p> Validate a user login. </p>
 */
public  class LoginAction  extends ActionSupport {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6637502602358242851L;
	static final Logger logger = Logger.getLogger(LoginAction.class);
	String error = null;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String execute() throws Exception {
		DOMConfigurator.configure("/laLlibreria/workspace/laLlibreria/src/log4j.xml");
		logger.debug("Sample debug message");
		logger.info("Sample info message");
		logger.warn("Sample warn message");
		logger.error("Sample error message");
		logger.fatal("Sample fatal message");
        System.out.println("Validating login");
    if(!getUsername().equals("Admin") || !getPassword().equals("Admin")){
            this.error = "Has possat la pota! Mira aqui dalt i torna-ho a intentar!";
            return ERROR;
    }
    else{
      Map session = ActionContext.getContext().getSession();
      session.put("username", getUsername());
      //session.remove("sessionLoginFail");
      return SUCCESS;
    }
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

}
