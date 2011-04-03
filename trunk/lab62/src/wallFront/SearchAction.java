/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;


import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Vector;

import util.TimeService;
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
		
		//Connectar-se amb el Webservice passant-li el serach key i tractar la informacio que es rebi
		
		//Vector per guardar els resultats
		
		Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
		
		HashMap<String,String> resultHash = new HashMap<String, String>();

		resultHash.put("resultID", "isbn");
		resultHash.put("titol", "La plaça del diamant");
		resultHash.put("autor", "Merce Rodoreda");

		results.addElement(resultHash);
		
		HashMap<String,String> resultHash2 = new HashMap<String, String>();
		
		resultHash2.put("resultID", "isbn2");
		resultHash2.put("titol", searchKey);
		resultHash2.put("autor", "Desconegut");

		results.addElement(resultHash2);
		
		request.getSession().setAttribute("results",results);
		return (mapping.findForward("success"));
	}

}
