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

public class WallTX extends Transaction {

	public WallTX() {
		super();
	}

	@Override
	public void execute() throws WallException {

		Connection con = (Connection) this.getParameterMap().get("dbConnection");
		SimpleDateFormat mySQLTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String userNICK = (String)this.getParameterMap().get("userNICK");
		Vector<HashMap<String, String>> wallPosts = new Vector<HashMap<String, String>>();
		int deletable = 0;
		try {
			Statement stmt = con.createStatement();
			String query = "select * from wallposts order by wpoTIME desc";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				HashMap<String,String> postHash = new HashMap<String, String>();

				String postid = rs.getString("wpoID");
				String wunick = rs.getString("wusNICK");
				postHash.put("postID", postid);
				postHash.put("authorNICK", wunick);
				postHash.put("content", rs.getString("wpoTEXT"));
				postHash.put("votes", rs.getString("wpoVOTES"));
				java.util.Date temps = mySQLTimeStamp.parse(rs.getString("wpoTIME"), new ParsePosition(0));
				postHash.put("postDate", TimeService.getDate(temps));
				postHash.put("postHour", TimeService.getHour(temps));
				if (userNICK !=null && userNICK.equals(wunick)) deletable++;
				wallPosts.addElement(postHash);
			}
			this.getParameterMap().put("wallPosts", wallPosts);
			this.getParameterMap().put("deletable", new Integer(deletable));
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) { throw new WallException(ex);}
	}
}
