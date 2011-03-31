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


public class InsertTX extends Transaction {


	public InsertTX() {
		super();
	}

	@Override
	public void execute() throws  WallException
	{
		Connection con = (Connection) this.getParameterMap().get("dbConnection");
		String userNICK = (String) this.getParameterMap().get("userNICK");
		String content = (String) this.getParameterMap().get("content");
		try {
			PreparedStatement stmt = con.prepareStatement("insert into wallposts (wusNICK, wpoTEXT) values (?, ?)");
			stmt.setString(1, userNICK);
			stmt.setString(2, content);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException ex) { throw new WallException(ex);}
	}

}
