package com.vaannila.ws;


import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import com.vaannila.domain.Llibre;

public class ISBNdbWS {
	//private String url = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=title&value1=sherlock+holmes";
	private static String accessKey = "OYPGEY8Z";
	//private boolean details;
	//private boolean texts;

    public static Integer numberResults(String keySearch) {
        String title = keySearch;
        String author = keySearch;
        title = title.replace(" ", "+");
        author = author.replace(" ", "+");
        /*Keys:
         *   	Joaquin	3TL9RX6R  OYPGEY8Z
         * 		Marc	8949LIQR
         */
        String isbndbUrl = "http://isbndb.com/api/books.xml?access_key="+accessKey+"&index1=combined&value1="+title+"+by+"+author;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(isbndbUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookList");
            
            Node bookData = elementNodeList.item(0);
            
            return Integer.parseInt(bookData.getAttributes().item(3).getNodeValue());
            
        } catch (Exception e) {
            return 0;
        }
    }
	
    public static ArrayList<Llibre> search(String keySearch, String page) {
        String title = keySearch;
        String author = keySearch;
        title = title.replace(" ", "+");
        author = author.replace(" ", "+");

        String isbndbUrl = "http://isbndb.com/api/books.xml?access_key="+accessKey+"&page_number="+page+"&index1=combined&value1="+title+"+by+"+author;
        
        return fetchList(isbndbUrl);
    }

    private static ArrayList<Llibre> fetchList(String requestUrl) {
    	ArrayList<Llibre> results = new ArrayList<Llibre>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookData");
            
            if(elementNodeList.getLength()==0) return null;
            
            for(int i=0; i<elementNodeList.getLength(); i++) {    
            	Llibre book = new Llibre();
            	
            	Node bookData = elementNodeList.item(i);
            	for(int j=0; j<bookData.getAttributes().getLength(); j++) {
            		if(bookData.getAttributes().item(j).getNodeName().equals("isbn13")) {
            			book.setIsbn(bookData.getAttributes().item(j).getNodeValue());
            		}
            	}
            	for(int j=0; j<bookData.getChildNodes().getLength(); j++) {
            		if(bookData.getChildNodes().item(j).getNodeName().equals("Title")) {
            			book.setTitle(bookData.getChildNodes().item(j).getTextContent());
            		}
            	}
            	for(int j=0; j<bookData.getChildNodes().getLength(); j++) {
            		if(bookData.getChildNodes().item(j).getNodeName().equals("AuthorsText")) {
            			book.setAuthor(bookData.getChildNodes().item(j).getTextContent());
            		}
            	}
            	
    			results.add(book);
            }
            
            return results;
                        
        } catch (Exception e) {
            return null;
        }
    }

	public static Llibre searchByISBN(String isbn) {
		
		String requestUrl = "http://isbndb.com/api/books.xml?access_key="+accessKey+"&index1=isbn&value1="+isbn;
		
		Llibre book = new Llibre();
		
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookData");
            
            if(elementNodeList.getLength()==0) return null;
            	
            Node bookData = elementNodeList.item(0);
            for(int j=0; j<bookData.getChildNodes().getLength(); j++) {
            	if(bookData.getChildNodes().item(j).getNodeName().equals("Title")) {
            		book.setTitle(bookData.getChildNodes().item(j).getTextContent());
            	}
            }
            for(int j=0; j<bookData.getChildNodes().getLength(); j++) {
            	if(bookData.getChildNodes().item(j).getNodeName().equals("AuthorsText")) {
            		book.setAuthor(bookData.getChildNodes().item(j).getTextContent());
            	}
            }

    		book.setIsbn(isbn);
            return book;
                        
        } catch (Exception e) {
        	Llibre book2 = new Llibre();
        	book2.setIsbn(isbn);
            return book2;
        }
    }
}
