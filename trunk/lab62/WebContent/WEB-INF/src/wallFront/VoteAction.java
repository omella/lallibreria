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
import org.apache.struts.action.DynaActionForm;

import wallBack.Transaction;
import wallBack.TransactionFactory;
import wallBack.WallException;



public class VoteAction extends Action {

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		Integer postID = (Integer)((DynaActionForm) form).get("postid");

		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");
		HashSet<Integer> table = (HashSet<Integer>) request.getSession().getAttribute("votedTable");
		if (table == null) {
			table = new HashSet<Integer>();
			request.getSession().setAttribute("votedTable", table);
		}
		if (!table.contains(postID)) {
			try {
				Transaction trans = txf.newTransaction("VoteTX");
				trans.getParameterMap().put("postID", postID);
				trans.execute();
				table.add(postID);
			}
			catch (WallException ex) {
				request.getSession().setAttribute("theList",ex.getMessageList());
				return (mapping.findForward("failure"));
			}
		}
		return (mapping.findForward("success"));
	}
}
