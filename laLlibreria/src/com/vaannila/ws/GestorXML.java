package com.vaannila.ws;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.catalina.util.ParameterMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;


import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

public class GestorXML {
	
	public static String createDocument(Vector<ParameterMap<String, String> > list) {
        // Create the root element
        Element comandaElement = new Element("comanda");
        comandaElement.setAttribute("Id", "data");
        //create the document
        Document myDocument = new Document(comandaElement);
        for (int i = 0; i < list.size(); ++i)
        {
        	ParameterMap<String,String> ll = list.elementAt(i);
        	Element llibreElement = new Element("llibre");
        	llibreElement.addContent(new Element("isbn").addContent(ll.get("isbn")));
        	llibreElement.addContent(new Element("titol").addContent(ll.get("titol")));
        	llibreElement.addContent(new Element("quantitat").addContent(ll.get("num")));
        	comandaElement.addContent(llibreElement);
        }

        XMLOutputter output = new XMLOutputter();
        return 	output.outputString(myDocument);
    }
	
	   public static String conexionPOST(String request, String datos, String protocolo) {

	        String responce = "";

	        OutputStreamWriter wr = null;

	        BufferedReader rd = null;

	        try {

	            URL url = new URL(request);



	            if (protocolo.equals("HTTPS")) {

	                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


	                //Escribir los parametros en el mensaje

	                conn.setDoOutput(true);

	                wr = new OutputStreamWriter(conn.getOutputStream());

	                wr.write(datos);

	                wr.flush();



	                //Recibir respuesta

	                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	            } else {

	                URLConnection conn = url.openConnection();

	                //Escribir los parametros en el mensaje

	                conn.setDoOutput(true);

	                wr = new OutputStreamWriter(conn.getOutputStream());

	                wr.write(datos);

	                wr.flush();



	                //Recibir respuesta

	                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            System.out.println("Resp Code:"+ ((HttpURLConnection) conn).getResponseCode()); 
					System.out.println("Resp Message:"+ ((HttpURLConnection) conn).getResponseMessage()); 
	            }





	            String line;



	            while ((line = rd.readLine()) != null) {

	                //Process line...

	                responce += line;

	            }



	        } catch (Exception e) {

	        } finally {

	            try {

	                if (wr != null) {

	                    wr.close();

	                }

	                if (rd != null) {

	                    rd.close();

	                }

	            } catch (IOException ex) {

	                System.out.println("Exception al cerrar el lector o el escritor");

	            }

	        }

	        return responce;

	    }

}
