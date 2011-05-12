

package com.vaannila.ws;

import com.google.gdata.client.Service;
import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
import com.safelayer.trustedx.client.smartwrapper.SmartDecryptResponse.thisRecipientInfo;
import com.vaannila.domain.Llibre;
import com.vaannila.ws.GoogleSimpleCommandLineParser;
import com.google.gdata.data.Category;
import com.google.gdata.data.Person;
import com.google.gdata.data.books.BooksCategory;
import com.google.gdata.data.books.VolumeEntry;
import com.google.gdata.data.books.VolumeFeed;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Identifier;
import com.google.gdata.data.dublincore.Title;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class GoogleBooksWS {
  protected GoogleBooksWS() {}

  public static Integer numberResults(String keySearch) {
		try {
			VolumeQuery query = new VolumeQuery(new URL("http://www.google.com/books/feeds/volumes"));
			//search for "keyword"
			query.setFullTextQuery(keySearch);
				BooksService service = new BooksService("gdataSample-Books-1");
				try {
					// Search for books
					VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
					volumeFeed.getEntries().size();
					return volumeFeed.getTotalResults();
				} catch (IOException e) {
					return 0;
				} catch (ServiceException e) {
					return 0;
				}	
		} catch (MalformedURLException e1) {
			return 0;
		}
  }
  
  public static List<Llibre> serchBook(String keyword, String page) {
	List<Llibre> resultat = new ArrayList<Llibre>();
	try {
		VolumeQuery query = new VolumeQuery(new URL("http://www.google.com/books/feeds/volumes"));
		//search for "keyword"
		query.setFullTextQuery(keyword);
	    query.setMaxResults(10);
	    query.setStartIndex((Integer.valueOf(page)*10)+1);
	    
//			System.out.println();
//		    System.out.println(query.getUrl());
//		    System.out.println();
	    
			BooksService service = new BooksService("gdataSample-Books-1");

			try {
				// Search for books
				VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
				
	            for (VolumeEntry entry : volumeFeed.getEntries()) {
	                Llibre libro = new Llibre();
	                libro = fetchBook(entry);
	                resultat.add(libro);
	            }
				return  resultat;
			} catch (IOException e) {
				Llibre ll = new Llibre();
				ll.setTitle("IOException");
				resultat.add(ll);
				return resultat;
			} catch (ServiceException e) {
				Llibre ll = new Llibre();
				ll.setTitle("ServiceException");
				resultat.add(ll);
				return resultat;
			}	
	} catch (MalformedURLException e1) {
		Llibre ll = new Llibre();
		ll.setTitle("MalformedURLException");
		resultat.add(ll);
		return resultat;
	}
}

public static Llibre getBook(String isbn) {
		Llibre resultat = new Llibre();
		try {
			VolumeQuery query = new VolumeQuery(new URL("http://www.google.com/books/feeds/volumes"));
			//search for "keyword"
			query.setFullTextQuery(isbn);
		    query.setMaxResults(1);
		    
//			System.out.println();
//			System.out.println(query.getUrl());
//			System.out.println();
			    
			BooksService service = new BooksService("gdataSample-Books-1");

				try {
					// Search for books
					VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
		            for (VolumeEntry entry : volumeFeed.getEntries()) {
		            	resultat=fetchBook(entry);
		            	return resultat;
		            }
		            return resultat;
				} catch (IOException e) {
					resultat.setTitle("IOException");
					return resultat;
				} catch (ServiceException e) {
					resultat.setTitle("ServiceException");
					return resultat;
				}	
		} catch (MalformedURLException e1) {
			resultat.setTitle("MalformedURLException");
			return resultat;
		}
	}

private static Llibre fetchBook(VolumeEntry entry) {
	Llibre resultat = new Llibre();
	resultat.setTitle(entry.getTitles().get(0).getValue());
    StringBuilder sb = new StringBuilder();
    boolean primero = true;
    for (Person p : entry.getAuthors()) {
        if (primero) {
            primero = false;
        } else {
            sb.append(" / ");
        }
        sb.append(p.getName());
    }
    for (Creator p : entry.getCreators()) {
        if (primero) {
            primero = false;
        } else {
            sb.append(" / ");
        }
        sb.append(p.getValue());
    }
    resultat.setAuthor(sb.toString());
    for (Identifier i : entry.getIdentifiers()) {
    	String tmp = i.getValue();
    	if(tmp.contains("ISBN:")) {
    		resultat.setIsbn(tmp.replace("ISBN:", ""));
    	}
    }
    if (entry.hasDescriptions()) {
    	resultat.setDescription(entry.getDescriptions().get(0).getValue());
    }
    if (entry.hasPublishers()) {
    	resultat.setPublisher(entry.getPublishers().get(0).getValue());
    }
    if (entry.hasSubjects()) {
    	resultat.setGenre(entry.getSubjects().get(0).getValue());
    }
    if (entry.getThumbnailLink() != null) {
    	resultat.setThumb(entry.getThumbnailLink().getHref().replace("zoom=5", "zoom=1"));
    	resultat.setCover(entry.getThumbnailLink().getHref().replace("zoom=5", "zoom=0"));
    }
    if (entry.getPreviewLink() != null) {
    	resultat.setPreview(entry.getPreviewLink().getHref().replace("f=false", "f=true"));
    }
    if (entry.hasLanguages()) {
    	String lang = entry.getLanguages().get(0).getValue();
    	if(lang.equals("es")) lang="Espanyol";
    	if(lang.equals("en")) lang="Angles";
    	if(lang.equals("fr")) lang="Frances";
    	if(lang.equals("ca")) lang="Catala";
    	if(lang.equals("it")) lang="Italia";
    	resultat.setLanguage(lang);
    }
    if (entry.hasDates()) {
    	resultat.setYear(entry.getDates().get(0).getValue());
    }
	//if (entry.hasFormats()) {
	//  libro.setDato("encuadernacion", entry.getFormats().get(0).getValue());
	//}
    return  resultat;
}

}


    