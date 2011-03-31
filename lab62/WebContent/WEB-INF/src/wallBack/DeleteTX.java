
package wallBack;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author farre
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteTX extends Transaction {



	public DeleteTX() {
		super();
	}

	@Override
	public void execute() throws  WallException {

		Connection dbConnection = (Connection) this.getParameterMap().get("dbConnection");
		String[] postIDs = (String []) this.getParameterMap().get("postIDs");

		if (postIDs != null) {
			try {
				Statement stmt = dbConnection.createStatement();
				String allMsgs = "(";
				for (String pid : postIDs)
					allMsgs = allMsgs + pid + ", ";
				allMsgs = allMsgs+"0)";
				String  deletePosts =
					"delete from wallposts where wpoID in "+allMsgs;
				dbConnection.setAutoCommit(false);
				try {
					stmt.executeUpdate(deletePosts);
					dbConnection.commit();
				}
				catch (SQLException ex) {
					dbConnection.rollback();
					throw ex;
				}
				finally {
					dbConnection.setAutoCommit(true);
					stmt.close();

				}
			}
			catch (SQLException ex) { throw new WallException(ex);}

		}
	}

}
