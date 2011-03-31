/*
 * Created on 16-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallBack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.MD5Encoder;


public class RegisterTX extends Transaction {


	public RegisterTX() {
		super();
	}

	@Override
	public void execute() throws  WallException
	{
		Connection con = (Connection) this.getParameterMap().get("dbConnection");
		
		String userNICK = (String) this.getParameterMap().get("userNICK");
		String userName = (String) this.getParameterMap().get("userNAME");
		String userPassword = (String) this.getParameterMap().get("userPassword");
		PreparedStatement pst = null;
		try {
			String insert = "insert into wallusers values (?, ?, ?)";
			pst = con.prepareStatement(insert);
			pst.setString(1, userNICK);
			pst.setString(2, userName);
			pst.setString(3, MD5Encoder.encode(userPassword));
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException ex ) {
			if (ex.getErrorCode() == 23001) {  // Nick ja existeix a la BD
				try {
					pst.close();
				}
				catch (SQLException ex2 ) {}
				throw new WallException("User's Nick '"+userNICK+"' is already taken by another user");
			}
			else throw new WallException(ex);
		}
	}
}
