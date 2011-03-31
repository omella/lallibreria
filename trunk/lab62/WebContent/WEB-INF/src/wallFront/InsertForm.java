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
public class InsertForm extends ActionForm{

	/**
	 *
	 */
	private static final long serialVersionUID = -7402159634109649384L;
	protected String message_body;

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.message_body = "";
	}

	public String getMessage_body() {
		return message_body;
	}

	public void setMessage_body(String content) {
		this.message_body = content;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// Check for mandatory data
		ActionErrors errors = new ActionErrors();
		if (message_body == null || message_body.equals(""))
			errors.add("wrongPostContent", new ActionMessage("errors.post.content"));
		return errors;
	}

}
