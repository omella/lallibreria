package com.vaannila.ws;

import java.util.List;

import com.vaannila.domain.Llibre;

public class BooksWS{

	public static List<Llibre> serchBook(String keyword, String page) {
		return ISBNdbWS.search(keyword, page);
	}

	public static Llibre getBook(String isbn) {
		return ISBNdbWS.searchByISBN(isbn);
	}

}
