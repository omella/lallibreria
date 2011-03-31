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


public class RegisterAction extends Action {


	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)

	{
		String userNICK = ((RegistrationForm) form).getUserNick();
		String userName= ((RegistrationForm) form).getUserFullName();
		String userPassword = ((RegistrationForm) form).getUserPassword();

		TransactionFactory txf = (TransactionFactory) request.getSession().getServletContext().getAttribute("transactionFactory");

		try {
			Transaction trans = txf.newTransaction("RegisterTX");
			trans.getParameterMap().put("userNICK", userNICK);
			trans.getParameterMap().put("userNAME", userName);
			trans.getParameterMap().put("userPassword", userPassword);
			trans.execute();
			request.getSession().setAttribute("sessionUserName",userName);
			request.getSession().setAttribute("sessionUserNICK",userNICK);
			return (mapping.findForward("success"));
		}
		catch (WallException ex) {
			if (ex.getMessageList().elementAt(0).startsWith("User's Nick")){
				ActionMessages errors = new ActionMessages();
				errors.add("regDuplicate", new ActionMessage("errors.duplicateUser",userNICK));
				this.saveErrors(request, errors);
				form.reset(mapping,request);
				return (mapping.findForward("duplicateUser"));
			}
			else {
				request.setAttribute("theList",ex.getMessageList());
				request.setAttribute("goBack", "wall_register.vt");
				return (mapping.findForward("failure"));
			}
		}

	}

}

