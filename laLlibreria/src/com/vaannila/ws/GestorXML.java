package com.vaannila.ws;

import java.io.*;

import org.apache.catalina.util.ParameterMap;

import org.jdom.Document;
import org.jdom.Element;

import org.jdom.output.XMLOutputter;

import java.util.Vector;

public class GestorXML {
	
	public static String createDocument(Vector<ParameterMap<String, String> > list) {
        // Create the root element
        Element comandaElement = new Element("comanda");
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

}
