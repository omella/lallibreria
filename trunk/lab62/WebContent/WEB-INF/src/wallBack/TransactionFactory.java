package wallBack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class TransactionFactory implements PlugIn{

	private String dbDriverClass;
	private String dbUsername;
	private String dbPassword;
	private String dbURI;
	private String dbPath;
	private String dbFullPath;
	private Connection dbConnection; 



	public void setDbDriverClass(String dbDriverClass) {
		this.dbDriverClass = dbDriverClass;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public void setDbURI(String dbURI) {
		this.dbURI = dbURI;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public void init(ActionServlet servlet, ModuleConfig conf)
	throws ServletException {
		try
		{
			Class.forName(dbDriverClass);
			dbFullPath = servlet.getServletContext().getRealPath("/")+this.dbPath; //$NON-NLS-1$ //$NON-NLS-2$
			servlet.getServletContext( ).setAttribute("transactionFactory", this);
		}
		catch (ClassNotFoundException fallo) {
			String errMiss = "Database access error (Driver '"+dbDriverClass+"' is unknown)";
			throw new ServletException(errMiss);
		}
	}

	public void destroy( ) {
		try {
			if (dbConnection != null && !dbConnection.isClosed()) 		
				dbConnection.close();
		}
		catch (SQLException ex ) {}
	}

	public Transaction newTransaction(String classname) throws WallException {
		Transaction trans;
		String commandClassName = "wallBack." + classname;
		try {
			trans = (Transaction) Class.forName(commandClassName).newInstance();
		}
		catch (Exception e) {
			throw new WallException("Unknown Transaction Class "+classname);
		}
		trans.getParameterMap().put("dbConnection", this.getConnection());
		return trans;
	}

	private Connection getConnection() throws WallException {
		try {
			if (dbConnection == null || dbConnection.isClosed()) 		
				dbConnection = DriverManager.getConnection(this.dbURI+this.dbFullPath, this.dbUsername, this.dbPassword);
		}
		catch (SQLException ex ) {
			throw new WallException(ex);
		}
		return dbConnection;
	}
}
