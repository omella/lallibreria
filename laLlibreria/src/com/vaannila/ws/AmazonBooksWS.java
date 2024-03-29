package com.vaannila.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vaannila.domain.Llibre;

/**
 * This class provides access to the Amazon E-Commerce API using Book.
 * @author Joaquin Omella
 */
public class AmazonBooksWS {
        
        /**
         * The AWS Access Key ID, used to access the Amazon API.
         */
        private final String AWS_ACCESS_KEY_ID = "AKIAIBFOONAHKLG3JLBA";
        
        /**
         * Your AWS Secret Key corresponding to the above ID.
         */
        private final String AWS_SECRET_KEY = "OBGMjJv+g2pNVFEkc9tBPWk/I/mLBHpmlqS2In8w";
        
        /** End-point used to connect to the Amazon API. */
        private final String ENDPOINT = "ecs.amazonaws.com";
        
        /** ISBNdb key 
         *   	Joaquin	3TL9RX6R
         * 		Marc	8949LIQR
         */
        private final String ISBNDB_KEY = "3TL9RX6R";
        
        /** Code from Amazon, used to sign the URL. */
        AmazonSignedRequestsHelper helper;
        
        /** Defines a factory API that enables applications to obtain a parser that produces DOM object trees from XML documents. */
        private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        /** Used for accessing APIs */
        private DocumentBuilder db;
        
        private boolean init = false;
        
        /**
         * Constructor, sets up signed request helper.
         * @throws NoSuchAlgorithmException 
         * @throws UnsupportedEncodingException 
         * @throws IllegalArgumentException If Amazon's signed request helper receives an illegal argument
         * @throws InvalidKeyException 
         * @throws ParserConfigurationException If there is a problem creating the document builder.
         */
        public AmazonBooksWS() {
        	try {
        		this.init=false;
                helper = AmazonSignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
                this.db = dbf.newDocumentBuilder();
                this.init=true;
	        } catch (Exception e) {
	        	this.init=false;
	        }
        }
        
        public boolean isInit() {
			return init;
		}

		// TODO create default "no cover art" image
        // Use this for filling by ISBN w/keywords as ISBN?
        /**
         * Finds the ISBN for the given Book using the following process:
         * Search amazon, show multiple results if necessary.
         * For each image result, look up info from ASIN, one page at a time 
         * Save ASIN as ISBN
         * @param b Book to determine the ISBN for.
         * @throws IOException 
         * @throws SAXException
         * @return true if info is added, false if book not found 
         */
        public boolean fillBookInfo(Llibre b) {
        	if(!this.isInit()) return false;
        	try {
                // Determine lookup keywords- ISBN or title + author
                String keywords = "";
                if (b.getIsbn() != "" && b.getIsbn() != null) keywords = b.getIsbn();
                else {
                        if (b.getTitle() != "" && b.getTitle() != null) keywords += b.getTitle().replace(" ", "+");
                        if (b.getAuthor() != "" && b.getAuthor() != null) {
                                if (keywords != "") keywords += "+"; // + between title and author if needed
                                keywords += b.getAuthor().replace(" ", "+");
                        }
                }
                
                // Set up lookup parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("Service", "AWSECommerceService");
                params.put("Version", "2009-03-31");
                params.put("Operation", "ItemSearch");
                params.put("SearchIndex", "Books");
                params.put("ResponseGroup", "Images");
                params.put("Keywords", keywords);

                // Sign URL, perform API request
                String requestUrl = helper.sign(params);
                Document doc = db.parse(requestUrl);
                
                // Go through results
                NodeList results = doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes();
                ArrayList<Llibre> bookResults = new ArrayList<Llibre>();
                for (int i=3; i<results.getLength(); i++) {
                        // One result at a time, create a book, populate info
                        NodeList curItem = results.item(i).getChildNodes();
                        Llibre result = new Llibre();
                        
                        // If there is cover art for this result, use it, otherwise, use default "none found" image
                        if (curItem.getLength() >= 4) {
                                result.setCover(new String(curItem.item(3).getFirstChild().getTextContent()));
                                result.setThumb(new String(curItem.item(2).getFirstChild().getTextContent()));
                        }
                        else result.setCover(null); // TODO Create default image
                        
                        // Here, ASIN == 10 digit ISBN, I'll just use the ISBN given by Amazon no matter what
                        result.setIsbn(curItem.item(0).getTextContent());
                         
                        fillOtherInfo(result);
                        bookResults.add(result);
                }
                
                Llibre correctResult = null;
                if (results.getLength() == 0) return false;
                else if (results.getLength() == 1) correctResult = bookResults.get(0);
                // Another test, like more than one with cover art?
                // TODO display some kind of window with images
                else if (results.getLength() > 1) {   
                        correctResult = bookResults.get(0);
                }
                
                b.mergeBookInfo(correctResult);
                
                return true;
        	} catch (Exception e) {
        		return false;
    	    }
        }
        
        /**
         * Fills title, author, publisher from Amazon API
         * @param b
         * @throws SAXException
         * @throws IOException
         */
        private void fillOtherInfo(Llibre b) throws SAXException, IOException {
                // Set up lookup parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("Service", "AWSECommerceService");
                params.put("Version", "2009-03-31");
                params.put("Operation", "ItemLookup");
                params.put("ItemId", b.getIsbn());

                // Sign URL, perform API request
                String requestUrl = helper.sign(params);
                Document doc = db.parse(requestUrl);
                
                if(doc.getElementsByTagName("Title").getLength()>0) {
                	b.setTitle(doc.getElementsByTagName("Title").item(0).getTextContent());
                }
                if(doc.getElementsByTagName("Author").getLength()>0) {
                	b.setAuthor(doc.getElementsByTagName("Author").item(0).getTextContent());
                }
                if(doc.getElementsByTagName("Manufacturer").getLength()>0) {
                	b.setPublisher(doc.getElementsByTagName("Manufacturer").item(0).getTextContent());
                }
        }

        /**
         * Will return the list of books similar to the given book.
         * @param b The book to find similar books to.
         * @return An ArrayList of books similar to the given book.
         */
        public List<Llibre> getSimilartoBook(String isbn) {
            List<Llibre> sim = new ArrayList<Llibre>();
            if(!this.isInit()) return sim;
            if(isbn==null) {
            	return sim;
            }
            Map<String, String> params = new HashMap<String, String>();
            
            String amazonId = getAmazonIdBook(isbn);
            if(amazonId==null) {
            	return sim;
            }
            
	        params.put("Service", "AWSECommerceService");
	        params.put("Version", "2009-03-31");
	        params.put("Operation", "ItemLookup");
	        params.put("ResponseGroup", "Similarities");
	        params.put("ItemId", amazonId);
	        
	        String requestUrl = helper.sign(params);
	          
	        try {
	        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder db = dbf.newDocumentBuilder();
	        	Document doc = db.parse(requestUrl);
	        	NodeList titleList = doc.getElementsByTagName("Title");
	        	for (int i=0; i<titleList.getLength() && i<5; i++) {
	        		String title = titleList.item(i).getTextContent();
	        		if(title.length()>50) title = title.substring(0, 50);
	        		if(BooksWS.numberResults(title)>0) {
		        		List<Llibre> temp = BooksWS.serchBook(title,Integer.toString(1));
		        		if(temp.size()>0) {
		        			sim.add(temp.get(0));
		        		}
	        		}
	        	}
	        	return sim;
	        } catch (Exception e) {
	        	return sim;
	        }
        }
        
        private String getAmazonIdBook(String isbn) {
            String amazonId = new String();
            if(!this.isInit()) return amazonId;
            Map<String, String> params = new HashMap<String, String>();
        
	        params.put("Service", "AWSECommerceService");
	        params.put("Version", "2009-03-31");
	        params.put("Operation", "ItemSearch");
	        params.put("SearchIndex", "Books");
            params.put("Keywords", isbn);
	
	        String requestUrl = helper.sign(params);
	          
	                try {
	                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                        DocumentBuilder db = dbf.newDocumentBuilder();
	                        Document doc = db.parse(requestUrl);
	                        NodeList asinList = doc.getElementsByTagName("ASIN"); 
	                        if(asinList.getLength()>0) {
	                        	amazonId = asinList.item(0).getTextContent();
	                        }                    
	                        return amazonId;
	                } catch (Exception e) {
	                	return amazonId;
	                }
        }
}
