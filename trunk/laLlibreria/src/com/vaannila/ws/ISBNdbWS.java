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


//import com.vaannila.domain.Llibre;

public class ISBNdbWS {
	//private String url = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=title&value1=sherlock+holmes";
	//private String accessKey = "3TL9RX6R";
	//private boolean details;
	//private boolean texts;

    public static ArrayList<HashMap<String, String>> search(String keySearch) {
        String title = keySearch;
        String author = keySearch;
        title = title.replace(" ", "+");
        author = author.replace(" ", "+");

        String isbndbUrl = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=combined&value1="+title+"+by+"+author;
        
        return fetchList(isbndbUrl);
    }

    private static ArrayList<HashMap<String, String>> fetchList(String requestUrl) {
    	ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookData");
            for(int i=0; i<elementNodeList.getLength(); i++) {    
            	HashMap<String,String> book = new HashMap<String,String>();
            	
            	Node bookData = elementNodeList.item(i);
            	
            	book.put("isbn", bookData.getAttributes().item(2).getNodeValue());
            	
            	NodeList nl = bookData.getChildNodes();

            	Element llibre = (Element)bookData;
            	
            	Element title = (Element) llibre.getElementsByTagName("TitleLong").item(0);
            	
	    		book.put("titol", title.getTextContent());
	    		book.put("autor", nl.item(5).getTextContent());
    			results.add(book);
            }
            
            return results;
                        
        } catch (Exception e) {
            HashMap<String,String> resultHash = new HashMap<String, String>();

			resultHash.put("isbn", "");
			resultHash.put("titol", "Error");
			resultHash.put("autor", "ISBNdb does not have a record for that title/author.");
	
			results.add(resultHash);
            return results;
        }
    }
    
    private static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static HashMap<String, String> searchISBN(String isbn) {
		
		String requestUrl = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=isbn&value1="+isbn;
		
		HashMap<String, String> book = new HashMap<String, String>();
		
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookData");            	
            Node bookData = elementNodeList.item(0);
            NodeList nl = bookData.getChildNodes();

            Element llibre = (Element)bookData;
            
            Element title = (Element) llibre.getElementsByTagName("TitleLong").item(0);
            	
	    	book.put("titol", title.getTextContent());
	    	book.put("autor", nl.item(5).getTextContent());

    		book.put("any", "No implementat");
    		book.put("isbn",isbn);
            return book;
                        
        } catch (Exception e) {
        	HashMap<String, String> resultHash = new HashMap<String, String>();
        	
			resultHash.put("isbn", "");
			resultHash.put("titol", "Error");
			resultHash.put("any", "");
			resultHash.put("autor", "ISBNdb does not have a record for that title/author.");

            return resultHash;
        }
    }
}
