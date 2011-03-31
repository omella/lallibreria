package wallBack;

import java.util.HashMap;


abstract public class Transaction {


	private HashMap<String,Object> parameterMap;

	public Transaction()
	{
		parameterMap = new HashMap<String,Object>();
	}

	public HashMap<String, Object> getParameterMap() {
		return parameterMap;
	}

	abstract public void execute() throws WallException;

}
