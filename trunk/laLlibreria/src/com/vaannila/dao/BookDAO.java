package com.vaannila.dao;

import java.util.HashMap;

public interface  BookDAO {

	public HashMap<String, String> getInfoBook(String isbn);
}
