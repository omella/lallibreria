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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import wallBack.Transaction;
import wallBack.TransactionFactory;
import wallBack.WallException;



public class LoginAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		String userNICK = ((LoginForm) form).getUser();
		String password = ((LoginForm) form).getPassword();
		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");

		try {
			Transaction trans = txf.newTransaction("LoginTX");
			trans.getParameterMap().put("userNICK", userNICK);
			trans.getParameterMap().put("password", password);
			trans.execute();
			request.getSession().setAttribute("sessionUserNICK",userNICK);
			request.getSession().setAttribute("sessionUserName",trans.getParameterMap().get("userNAME"));
			return (mapping.findForward("success"));
		}
		catch (WallException ex) {
			String exMess = ex.getMessageList().elementAt(0);
			if (exMess.startsWith("login")){
				ActionMessages errors = new ActionMessages();
				errors.add(exMess, new ActionMessage("errors."+exMess));
				this.saveErrors(request, errors);
				return (mapping.findForward("wrongData"));
			}
			else {
				request.setAttribute("theList",ex.getMessageList());
				return (mapping.findForward("failure"));
			}
		}
	}

}


