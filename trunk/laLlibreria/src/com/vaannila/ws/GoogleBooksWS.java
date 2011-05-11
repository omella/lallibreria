

package com.vaannila.ws;

import com.google.gdata.client.Service;
import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
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
	    
			System.out.println();
		    System.out.println(query.getUrl());
		    System.out.println();
		    
			BooksService service = new BooksService("gdataSample-Books-1");

			try {
				// Search for books
				System.out.println("Obtenim llibres");
				VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
				System.out.println("Processem el resultat");
	            for (VolumeEntry entry : volumeFeed.getEntries()) {
	                Llibre libro = new Llibre();
	                libro.setTitle(entry.getTitles().get(0).getValue());
	                System.out.println(entry.getTitles().get(0).getValue());
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
	                for (Identifier i : entry.getIdentifiers()) {
	                	String tmp = i.getValue();
	                	if(tmp.contains("ISBN:")) {
	                		libro.setIsbn(tmp.replace("ISBN:", ""));
	                	}
	                }
//	                if (entry.hasLanguages()) {
//	                    libro.setDato("idioma", entry.getLanguages().get(0).getValue());
//	                }
//	                if (entry.hasFormats()) {
//	                    libro.setDato("encuadernacion", entry.getFormats().get(0).getValue());
//	                }
	                //TODO Esto suele ser multilinea
	                if (entry.hasDescriptions()) {
	                    libro.setDescription(entry.getDescriptions().get(0).getValue());
	                }
	                if (entry.hasPublishers()) {
	                    libro.setPublisher(entry.getPublishers().get(0).getValue());
	                } else {
	                    libro.setPublisher("");
	                }
//	                if (entry.hasDates()) {
//	                    libro.("publicacion", libro.getDato("publicacion") + " " + entry.getDates().get(0).getValue());
//	                }
	                if (entry.hasSubjects()) {
	                    libro.setGenre(entry.getSubjects().get(0).getValue());
	                }

	                //TODO Añadir todos los datos que se puedan. Formatear fechas
	                //System.out.println(entry.getContributors());

	                if (entry.getThumbnailLink() != null) {
	                    libro.setThumb(entry.getThumbnailLink().getHref());
	                }
	                if (entry.getThumbnailLink() != null) {
	                    libro.setCover(entry.getThumbnailLink().getHref());
	                }
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
		    
			System.out.println();
			System.out.println(query.getUrl());
			System.out.println();
			    
			BooksService service = new BooksService("gdataSample-Books-1");

				try {
					// Search for books
					VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
		            for (VolumeEntry entry : volumeFeed.getEntries()) {
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
//		                if (entry.hasLanguages()) {
//		                    libro.setDato("idioma", entry.getLanguages().get(0).getValue());
//		                }
//		                if (entry.hasFormats()) {
//		                    libro.setDato("encuadernacion", entry.getFormats().get(0).getValue());
//		                }
		                //TODO Esto suele ser multilinea
		                if (entry.hasDescriptions()) {
		                	resultat.setDescription(entry.getDescriptions().get(0).getValue());
		                }
		                if (entry.hasPublishers()) {
		                	resultat.setPublisher(entry.getPublishers().get(0).getValue());
		                } else {
		                	resultat.setPublisher("");
		                }
//		                if (entry.hasDates()) {
//		                    libro.("publicacion", libro.getDato("publicacion") + " " + entry.getDates().get(0).getValue());
//		                }
		                if (entry.hasSubjects()) {
		                	resultat.setGenre(entry.getSubjects().get(0).getValue());
		                }

		                //TODO Añadir todos los datos que se puedan. Formatear fechas
		                //System.out.println(entry.getContributors());

		                if (entry.getThumbnailLink() != null) {
		                	resultat.setThumb(entry.getThumbnailLink().getHref());
		                }
		                if (entry.getThumbnailLink() != null) {
		                	resultat.setCover(entry.getThumbnailLink().getHref());
		                }       
		            }
					return  resultat;
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
// 
//  /**
//   * The name of the server hosting the YouTube GDATA feeds.
//   */
//  public static final String BOOKS_GDATA_SERVER = "http://books.google.com";
//
//
//  /**
//   * The URL of the volumes feed
//   */
//  public static final String VOLUMES_FEED = BOOKS_GDATA_SERVER
//      + "/books/feeds/volumes";
//
//  public static void main(String[] args) {
//	    GoogleSimpleCommandLineParser parser = new GoogleSimpleCommandLineParser(args);
//	    String username = parser.getValue("username", "user", "u");
//	    String password = parser.getValue("password", "pass", "p");
//	    boolean help = parser.containsKey("help", "h");
//	    boolean authenticated = (username != null) && (password != null);
//
//	    BooksService service = new BooksService("gdataSample-Books-1");
//
//	    if (authenticated) {
//	      try {
//	        service.setUserCredentials(username, password);
//	      } catch (AuthenticationException e) {
//	        System.out.println("Invalid login credentials.");
//	        System.exit(1);
//	      }
//	      // Search for books
//	      searchVolumes(service, authenticated);
//	    }
//  }
//  /**
//   * Searches the VOLUMES_FEED for search terms and print each resulting
//   * VolumeEntry.
//   *
//   * @param service a BooksService object.
//   * @param authenticated whether the user is authenticated.
//   * @throws ServiceException
//   *                     If the service is unable to handle the request.
//   * @throws IOException error sending request or reading the feed.
//   */
//  private static void searchVolumes(BooksService service,
//                                    boolean authenticated)
//      throws IOException, ServiceException {
//    VolumeQuery query = new VolumeQuery(new URL(VOLUMES_FEED));
//
//    // exclude no-preview book (by default, they are included)
//    query.setMinViewability(VolumeQuery.MinViewability.PARTIAL);
//
//    System.out.println("\nEnter search terms: ");
//    String searchTerms = readLine();
//    System.out.println();
//
//    query.setFullTextQuery(searchTerms);
//
//    printUnderlined("Running Search for '" + searchTerms + "'");
//    VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
//    printVolumeFeed(volumeFeed);
//  }
//
//  /**
//   * Prints a String, a newline, and a number of '-' characters equal to the
//   * String's length.
//   *
//   * @param stringToUnderline - the string to print underlined
//   */
//  private static void printUnderlined(String stringToUnderline) {
//    System.out.println(stringToUnderline);
//    for (int i = 0; i < stringToUnderline.length(); ++i) {
//      System.out.print("-");
//    }
//    System.out.println("\n");
//  }
//
//  /**
//   * Prints a VolumeEntry
//   *
//   * @param entry The VolumeEntry to be printed
//   * @throws IOException Error sending request or reading the feed.
//   * @throws ServiceException If the service is unable to handle the request.
//   */
//  private static void printVolumeEntry(VolumeEntry entry) throws IOException,
//      ServiceException {
//    System.out.print("Title: ");
//    for (Title t : entry.getTitles()) {
//      System.out.print(t.getValue() + "\t");
//    }
//    System.out.println();
//    System.out.print("Author: ");
//    for (Creator c : entry.getCreators()) {
//      System.out.print(c.getValue() + "\t");
//    }
//    System.out.println();
//    if (entry.hasRating()) {
//      System.out.println("Rating: " + entry.getRating().getAverage());
//    }
//    if (entry.hasReview()) {
//      System.out.println("Review: " + entry.getReview().getValue());
//    }
//    boolean firstLabel = true;
//    if (entry.getCategories().size() > 0) {
//      for (Category c : entry.getCategories()) {
//        if (c.getScheme() == BooksCategory.Scheme.LABELS_SCHEME) {
//          if (firstLabel) {
//            System.out.print("Labels: ");
//            firstLabel = false;
//          }
//          System.out.print(c.getTerm() + "\t");
//        }
//      }
//      if (!firstLabel) {
//        System.out.println();
//      }
//    }
//    if (entry.hasViewability()) {
//      System.out.println("Viewability: " + entry.getViewability().getValue());
//    }
//    System.out.println();
//  }
//
//  /**
//   * Show the feed of user annotations.
//   *
//   * @param service A BooksService object.
//   * @throws IOException Error sending request or reading the feed.
//   * @throws ServiceException If the service is unable to handle the request.
//   */
//  private static void showUserAnnotations(Service service)
//      throws IOException, ServiceException {
//    VolumeFeed volumeFeed = service.getFeed(new URL(USER_ANNOTATION_FEED),
//        VolumeFeed.class);
//    printVolumeFeed(volumeFeed);
//  }
//
//  /**
//   * Show the feed of user annotations.
//   *
//   * @param service A BooksService object.
//   * @throws IOException Error sending request or reading the feed.
//   * @throws ServiceException If the service is unable to handle the request.
//   */
//  private static void showUserLibrary(Service service)
//      throws IOException, ServiceException {
//    VolumeFeed volumeFeed = service.getFeed(new URL(USER_LIBRARY_FEED),
//        VolumeFeed.class);
//    printVolumeFeed(volumeFeed);
//  }
//
//  /**
//   * Solicits the user for the index of a volume in a list
//   *
//   * @param volumeFeed A volume Feed.
//   * @return String containing a volume ID.
//   * @throws IOException If there are problems reading user input.
//   */
//  private static String readVolumeId(VolumeFeed volumeFeed) throws IOException {
//    System.out.println("Input the index of one of the volumes listed above (1-"
//                       + volumeFeed.getEntries().size() + ")");
//
//    return volumeFeed.getEntries().get(0).getId();
//  }
//
//  /**
//   * Print a volume feed of entries as a numbered list
//   *
//   * @param volumeFeed A feed of volumes
//   * @throws IOException Error sending request or reading the feed.
//   * @throws ServiceException If the service is unable to handle the request.
//   */
//  private static void printVolumeFeed(VolumeFeed volumeFeed)
//      throws IOException, ServiceException {
//    String title = volumeFeed.getTitle().getPlainText();
//    System.out.println(title);
//
//    List<VolumeEntry> volumeEntries = volumeFeed.getEntries();
//    if (volumeEntries.size() == 0) {
//      System.out.println("This feed contains no entries.");
//      return;
//    }
//    System.out.println("Results " + volumeFeed.getStartIndex() + " - " +
//                       (volumeFeed.getStartIndex() + volumeEntries.size() - 1) +
//                       " of " + volumeFeed.getTotalResults());
//    System.out.println();
//
//    int count = 1;
//    for (VolumeEntry entry : volumeEntries) {
//      System.out.println("(Volume #" + String.valueOf(count) + ")");
//      printVolumeEntry(entry);
//      count++;
//    }
//    System.out.println();
//  }
}


    