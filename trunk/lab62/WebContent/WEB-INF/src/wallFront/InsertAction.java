/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;


import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import wallBack.WallException;
import wallBack.Transaction;
import wallBack.TransactionFactory;



public class InsertAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		String userNICK = (String) request.getSession().getAttribute("sessionUserNICK");
		String content = ((InsertForm) form).getMessage_body();
		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");

		Vector<String> validationErrorList = new Vector<String>();
		Boolean paramsOK = true;

		if (userNICK == null) { validationErrorList.addElement("You must login first"); paramsOK = false; }
		if (content.equals(""))  { validationErrorList.addElement("Content cannot be empty"); paramsOK = false;}

		if (paramsOK)
		{
			// InsertTX trans = new InsertTX(user, title, content);
			try {
				Transaction trans = txf.newTransaction("InsertTX");
				trans.getParameterMap().put("userNICK", userNICK);
				trans.getParameterMap().put("content", content);
				trans.execute();
				return (mapping.findForward("success"));
			}
			catch (WallException ex) {
				request.setAttribute("theList",ex.getMessageList());
				return (mapping.findForward("failure"));
			}
		}
		else {
			request.setAttribute("theList",validationErrorList);
			return (mapping.findForward("failure"));
		}
	}

}

