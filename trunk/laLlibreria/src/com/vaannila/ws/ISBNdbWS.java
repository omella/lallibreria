package com.vaannila.ws;


import java.util.HashMap;
import java.util.Vector;

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
	
    public static Vector<HashMap<String,String>> search(String keySearch) {
        String title = keySearch;
        String author = keySearch;
        title = title.replace(" ", "+");
        author = author.replace(" ", "+");

        String isbndbUrl = "http://isbndb.com/api/books.xml?access_key=3TL9RX6R&index1=combined&value1="+title+"+by+"+author;
        
        return fetchISBN(isbndbUrl);
    }

    private static Vector<HashMap<String,String>> fetchISBN(String requestUrl) {
    	Vector <HashMap<String,String> > results = new Vector <HashMap<String,String> >();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            NodeList elementNodeList = doc.getElementsByTagName("BookData");
            for(int i=0; i<elementNodeList.getLength(); i++) {    
            	HashMap<String,String> resultHash = new HashMap<String, String>();
            	
            	Node bookData = elementNodeList.item(i);
            	
            	resultHash.put("resultID", bookData.getAttributes().item(2).getNodeValue());
            	
            	NodeList nl = bookData.getChildNodes();

            	Element llibre = (Element)bookData;
            	
            	Element title = (Element) llibre.getElementsByTagName("TitleLong").item(0);
            	
	    		resultHash.put("titol", title.getTextContent());
	    		resultHash.put("autor", nl.item(5).getTextContent());
    			results.addElement(resultHash);
            }
            
            return results;
                        
        } catch (Exception e) {
            HashMap<String,String> resultHash = new HashMap<String, String>();

			resultHash.put("resultID", "");
			resultHash.put("titol", "Error");
			resultHash.put("autor", "ISBNdb does not have a record for that title/author.");
	
			results.addElement(resultHash);
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
}
