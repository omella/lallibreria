/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;


import java.util.Vector;

import wallFront.SearchForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class SearchAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		String searchKey = ((SearchForm) form).getSearchKey();
		
		//Connectar-se amb el Webservice passant-li el titol i tractar la informacio que es rebi
		
		//Guardar a la sessio els resultats obtinguts
		
		Vector<String> results = new Vector<String>();
		
		results.add(searchKey);
		
		results.add("result2");
		
		request.getSession().setAttribute("results",results);
		return (mapping.findForward("success"));
	}

}
