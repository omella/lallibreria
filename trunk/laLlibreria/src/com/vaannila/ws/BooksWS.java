package com.vaannila.ws;

import java.util.ArrayList;
import java.util.List;

import com.vaannila.domain.Llibre;

public class BooksWS{

	public static List<Llibre> serchBook(String keyword, String page) {
		AmazonBooksWS amazon = null;
		amazon = new AmazonBooksWS();
		List<Llibre> llistaLlibres = ISBNdbWS.search(keyword, page);
		List<Llibre> resultat = new ArrayList<Llibre>();
		if(llistaLlibres==null) return resultat;
		for (int i=0; i<llistaLlibres.size(); i++) {
			Llibre b = llistaLlibres.get(i);
			if(b!=null && amazon.isInit()) {
				amazon.fillBookInfo(b);
				resultat.add(b);
			}
		}
		return resultat;
	}

	public static Llibre getBook(String isbn) {
		Llibre result = ISBNdbWS.searchByISBN(isbn);
		AmazonBooksWS amazon = new AmazonBooksWS();
		if(amazon.isInit()) {
			amazon.fillBookInfo(result);
		}
		return result;
	}

}