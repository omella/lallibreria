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
public class SearchForm extends ActionForm{


	protected String searchKey;

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.searchKey = "";
	}

	public String getSearchKey() {
		return searchKey;
	}



	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}



	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// Check for mandatory data
		ActionErrors errors = new ActionErrors();
		//if (searchKey == null || searchKey.equals(""))
		//	errors.add("wrongSearchKey", new ActionMessage("errors.searchKey"));
		return errors;
	}

}
