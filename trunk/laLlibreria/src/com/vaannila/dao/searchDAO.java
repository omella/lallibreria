package com.vaannila.dao;

import java.util.ArrayList;

import com.vaannila.domain.Llibre;

public class searchDAO {
	

	public static ArrayList<String> listResults() {	
		ArrayList<String> results = new ArrayList<String>();
		try {
			results.add("Llibre1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	public static Llibre getInfoLlibre() {
		// TODO Auto-generated method stub
		return null;
	}

}
