/*
 * Created on 15-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallFront;


import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.TimeService;
import wallBack.Transaction;
import wallBack.TransactionFactory;
import wallBack.WallException;


public class BrowserAction extends Action {

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");
		String userNICK = (String) request.getSession().getAttribute("sessionUserNICK");
		HashSet<String> votedPosts = (HashSet<String>) request.getSession().getAttribute("votedTable");
		
		try {
			Transaction trans = txf.newTransaction("WallTX");
			trans.getParameterMap().put("userNICK", userNICK);
			trans.getParameterMap().put("votedTable", votedPosts);
			trans.execute();
			request.setAttribute("currentDate", TimeService.getCurrentDate());
			request.setAttribute("wallPosts",trans.getParameterMap().get("wallPosts"));
			request.setAttribute("deletable",trans.getParameterMap().get("deletable"));
			return (mapping.findForward("success"));
		}
		catch (WallException ex) {
			request.setAttribute("theList",ex.getMessageList());
			return (mapping.findForward("failure"));
		}

	}

}
