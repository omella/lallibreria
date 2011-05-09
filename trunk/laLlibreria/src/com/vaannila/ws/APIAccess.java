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

import com.amazon.advertising.api.sample.*;
import com.vaannila.domain.Llibre;

/**
 * This class provides access to the Amazon E-Commerce API using Book.
 * @author Joaquin Omella
 */
public class APIAccess {
        
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
        private final String ISBNDB_KEY = "8949LIQR";
        
        /** Code from Amazon, used to sign the URL. */
        SignedRequestsHelper helper;
        
        /** Defines a factory API that enables applications to obtain a parser that produces DOM object trees from XML documents. */
        private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        /** Used for accessing APIs */
        private DocumentBuilder db;
        
        /**
         * Constructor, sets up signed request helper.
         * @throws NoSuchAlgorithmException 
         * @throws UnsupportedEncodingException 
         * @throws IllegalArgumentException If Amazon's signed request helper receives an illegal argument
         * @throws InvalidKeyException 
         * @throws ParserConfigurationException If there is a problem creating the document builder.
         */
        public APIAccess() throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException, ParserConfigurationException {
                helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
                this.db = dbf.newDocumentBuilder();
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
        public boolean fillBookInfo(Llibre b) throws SAXException, IOException {
                // Determine lookup keywords- ISBN or title + author
                String keywords = "";
                if (b.getIsbn() != "" && b.getIsbn() != null) keywords = b.getIsbn();
                else {
                        if (b.getTitol() != "" && b.getTitol() != null) keywords += b.getTitol().replace(" ", "+");
                        if (b.getAutor() != "" && b.getAutor() != null) {
                                if (keywords != "") keywords += "+"; // + between title and author if needed
                                keywords += b.getAutor().replace(" ", "+");
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
                                result.setCoverArt(new String(curItem.item(3).getFirstChild().getTextContent()));
                                result.setThumb(new String(curItem.item(2).getFirstChild().getTextContent()));
                        }
                        else result.setCoverArt(null); // TODO Create default image
                        
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
                
                mergeBookInfo(b, correctResult);
                
                return true;
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
                	b.setTitol(doc.getElementsByTagName("Title").item(0).getTextContent());
                }
                if(doc.getElementsByTagName("Author").getLength()>0) {
                	b.setAutor(doc.getElementsByTagName("Author").item(0).getTextContent());
                }
                if(doc.getElementsByTagName("Manufacturer").getLength()>0) {
                	b.setPublisher(doc.getElementsByTagName("Manufacturer").item(0).getTextContent());
                }
        }

        /**
         * Merges book info from s into p
         * @param p Primary- the target
         * @param s Secondary- the info source
         */
        private void mergeBookInfo(Llibre p, Llibre s) {
                if (p.getAutor() == null) p.setAutor(s.getAutor());
                if (p.getCoverArt() == null) p.setCoverArt(s.getCoverArt());
                if (p.getGenre() == null) p.setGenre(s.getGenre());
                if (p.getIsbn() == null) p.setIsbn(s.getIsbn());
                if (p.getPublisher() == null) p.setPublisher(s.getPublisher());
                if (p.getSeries() == null) p.setSeries(s.getSeries());
                if (p.getSubtitle() == null) p.setSubtitle(s.getSubtitle());
                if (p.getThumb() == null) p.setThumb(s.getThumb());
                if (p.getTitol() == null) p.setTitol(s.getTitol());
        }

        /**
         * Will return the list of books similar to the given book.
         * @param b The book to find similar books to.
         * @return An ArrayList of books similar to the given book.
         */
        public ArrayList<Llibre> getSimilartoBook(Llibre b) {
                ArrayList<Llibre> sim = new ArrayList<Llibre>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ResponseGroup", "Similarities");
//        params.put("ItemId", Long.toString(b.getISBN()));

        String requestUrl = helper.sign(params);
          
                try {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(requestUrl);
                        NodeList titleList = doc.getElementsByTagName("Title");
                        NodeList authorList = doc.getElementsByTagName("Author");
                        //etc!
                        
                        for (int i=0; i<titleList.getLength(); i++) {
                                //sim.add(new Book(titleList.index(0)))
                        }
                } catch (Exception e) {
                        System.out.println(e);
                }
          
                return sim;
        }
        
        /**
         * Will return the list of books similar to the given list of books.
         * @param b The list of books to find similar books to.
         * @return An ArrayList of books similar to the given book.
         */
        public ArrayList<Llibre> getSimilartoBooks(List<Llibre> b) {
                ArrayList<Llibre> sim = new ArrayList<Llibre>();
                
                //getSimilartoBook()
                
                return sim;
        }

}
