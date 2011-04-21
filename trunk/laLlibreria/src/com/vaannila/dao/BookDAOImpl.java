package com.vaannila.dao;

import java.util.HashMap;

public class BookDAOImpl implements BookDAO{

	@Override
	public HashMap<String, String> getInfoBook(String isbn) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		//Connectar-se amb el WS per obtenir la info del llibre
		
		result.put("titol", "El Codigo Da Vinci");
		result.put("autor", "???");
		result.put("descripcio", "Mola que te cagas");
		
		return null;
	}

}
