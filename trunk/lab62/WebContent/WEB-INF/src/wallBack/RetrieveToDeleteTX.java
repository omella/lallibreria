package wallBack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import util.TimeService;

public class RetrieveToDeleteTX extends Transaction {

	public RetrieveToDeleteTX() {
		super();
	}

	@Override
	public void execute() throws WallException {

		Connection con = (Connection) this.getParameterMap().get("dbConnection");
		SimpleDateFormat mySQLTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String userNICK = (String)this.getParameterMap().get("userNICK");
		Vector<HashMap<String, String>> wallPosts = new Vector<HashMap<String, String>>();
		try {
			Statement stmt = con.createStatement();
			String query = "select * from wallposts where wusNICK = '" + userNICK + "' order by wpoTIME desc";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				HashMap<String,String> postHash = new HashMap<String, String>();
				postHash.put("postID", rs.getString(1));
				postHash.put("content", rs.getString(3));
				postHash.put("votes", rs.getString(4));
				java.util.Date temps = mySQLTimeStamp.parse(rs.getString(5), new ParsePosition(0));
				postHash.put("postDate", TimeService.getDate(temps));
				postHash.put("postHour", TimeService.getHour(temps));
				wallPosts.addElement(postHash);
			}
			this.getParameterMap().put("wallPosts", wallPosts);
			rs.close();
			stmt.close();
			con.close();
		}
		catch (SQLException ex) { throw new WallException(ex);}
	}
}
