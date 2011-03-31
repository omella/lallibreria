/*
 * Created on 16-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wallBack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.MD5Encoder;


/**
 * @author farre
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginTX extends Transaction {

	public LoginTX() {
		super();
	}

	@Override
	public void execute() throws WallException {
		Connection con = (Connection) this.getParameterMap().get("dbConnection");
		String userNICK = (String) this.getParameterMap().get("userNICK");
		String password = (String) this.getParameterMap().get("password");
		try {
			String  queryString = "select wusNAME,(? = wusPWD) from wallusers where wusNICK = ?";
			PreparedStatement stmt = con.prepareStatement(queryString);
			stmt.setString(1, MD5Encoder.encode(password));
			stmt.setString(2, userNICK);
			ResultSet rs = stmt.executeQuery();
			if (rs.first())
				if (rs.getBoolean(2))
					this.getParameterMap().put("userNAME", rs.getString(1));

				else throw new WallException("loginPassword");
			else
				throw new WallException("loginUser");
			rs.close();
			stmt.close();
		}
		catch (SQLException ex ) {throw new WallException(ex);
		}
	}
}
