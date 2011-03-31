/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import wallBack.Transaction;
import wallBack.TransactionFactory;
import wallBack.WallException;


public class DeleteAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{

		String[] postIDs = (String[])((DynaActionForm) form).get("deletePostID");
		
		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");

		try {

			Transaction trans = txf.newTransaction("DeleteTX");
			trans.getParameterMap().put("postIDs", postIDs);
			trans.execute();
			return (mapping.findForward("success"));
		}
		catch (WallException ex) {
			request.setAttribute("theList",ex.getMessageList());
			return (mapping.findForward("failure"));
		}
	}

}


