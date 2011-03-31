/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author farre
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginForm extends ActionForm{


	private static final long serialVersionUID = 7877001805519300712L;
	protected String user;
	protected String password;

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.user = "";
		this.password = "";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String username) {
		this.user = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String userpassword) {
		this.password = userpassword;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// Check for mandatory data
		ActionErrors errors = new ActionErrors();
		if (user == null || user.equals(""))
			errors.add("wrongUserNick", new ActionMessage("errors.usernick"));
		if (password == null || password.equals(""))
			errors.add("wrongPassword", new ActionMessage("errors.password"));
		return errors;
	}

}
