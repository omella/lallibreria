/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

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
public class RegistrationForm extends ActionForm{

	/**
	 *
	 */
	private String recaptcha_publicKey = "6Lc_QgUAAAAAAFyWFhYONWN22eeUrgGoF5W7EZ3D";
	private String recaptcha_privateKey = "6Lc_QgUAAAAAAOdDbdiPkpiguqVPhKlu5jK-ZSOd";

	private static final long serialVersionUID = -7402159634109649384L;
	protected String userNick;
	protected String userFullName;
	protected String userPassword;
	protected String recaptcha_challenge_field;
	protected String recaptcha_response_field;

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.userNick = "";
		this.userFullName = "";
		this.userPassword = "";
		this.recaptcha_challenge_field = "";
		this.recaptcha_response_field = "";
	}
	public String getUserNick() {
		return userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getRecaptcha_challenge_field() {
		return recaptcha_challenge_field;
	}
	public void setRecaptcha_challenge_field(String recaptcha_challenge_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
	}

	public String getRecaptcha_response_field() {
		return recaptcha_response_field;
	}
	public void setRecaptcha_response_field(String recaptcha_response_field) {
		this.recaptcha_response_field = recaptcha_response_field;
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// Check for mandatory data
		ActionErrors errors = new ActionErrors();
		if (userNick == null || userNick.equals(""))
			errors.add("wrongUserNick", new ActionMessage("errors.usernick"));
		if (userFullName == null || userFullName.equals(""))
			errors.add("wrongUserFullName", new ActionMessage("errors.username"));
		if (userPassword == null || userPassword.equals(""))
			errors.add("wrongPassword", new ActionMessage("errors.password"));
		
		try { // Sometimes the ReCaptcha does'nt work, so let's try ..
			ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(recaptcha_publicKey, recaptcha_privateKey, false);
			ReCaptchaResponse res = captcha.checkAnswer(request.getRemoteAddr(), recaptcha_challenge_field, recaptcha_response_field);

			if (!res.isValid())  errors.add("wrongCAPTCHA", new ActionMessage("errors.regCAPTCHA"));
		}
		catch (net.tanesha.recaptcha.ReCaptchaException cex) 
		{ 
			// If the ReCaptcha API does'nt work, let's assume that the validation is OK
		}
		
		return errors;
	}

}
