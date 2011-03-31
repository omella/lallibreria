
package wallBack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteTX extends Transaction {

	public VoteTX()
	{
		super();
	}


	public void execute() throws WallException
	{
		Connection dataCon = (Connection) this.getParameterMap().get("dbConnection");
		Integer postID = (Integer) this.getParameterMap().get("postID");
		try {
			PreparedStatement pst1 = dataCon.prepareStatement("update wallposts set wpoVOTES = wpoVOTES + 1 where wpoID = ?");
			pst1.setInt(1, postID.intValue());
			pst1.executeUpdate();
			pst1.close();
		}
		catch (SQLException ex) { throw new WallException(ex);}
	}

}
