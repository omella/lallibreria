package wallBack;

import java.sql.SQLException;
import java.util.Vector;

public class WallException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 6518764094242408959L;
	private Vector<String> messageList;

	public WallException() {
		// TODO Auto-generated constructor stub
		this.messageList = new Vector<String>();
	}

	public WallException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		this.messageList = new Vector<String>();
		this.messageList.addElement(arg0);
	}

	public WallException(SQLException ex) {
		super(ex);
		this.messageList = new Vector<String>();
		while (ex != null) {
			this.messageList.addElement("Message:   " + ex.getMessage ());
			this.messageList.addElement("SQLState:  " + ex.getSQLState ());
			this.messageList.addElement("ErrorCode: " + ex.getErrorCode ());
			ex = ex.getNextException();
		}
	}

	public Vector<String> getMessageList() {
		return messageList;
	}


}
