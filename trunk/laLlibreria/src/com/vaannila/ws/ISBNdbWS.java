package com.vaannila.ws;


import java.util.ArrayList;

import com.vaannila.domain.Llibre;

public class ISBNdbWS {
	private String url = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=title&value1=sherlock+holmes";
	private String accessKey = "3TL9RX6R";
	private boolean details;
	private boolean texts;
}
