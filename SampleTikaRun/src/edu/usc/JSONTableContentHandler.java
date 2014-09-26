package edu.usc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class JSONTableContentHandler extends ContentHandlerDecorator {

	StringBuffer buffer = null ;
	final List <String> skipList = new ArrayList<String>( Arrays.asList("html", "head", "meta", "title", "body"));

	public JSONTableContentHandler() {
	}

	public void startDocument() throws SAXException {
		buffer = new StringBuffer();
		buffer.append("{");
	}

	public void endDocument() throws SAXException {
		buffer.append("}");
 	}
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(ch.length > 0 && !skipList.contains(ch))
			buffer.append( "\""+ new String(ch) +"\" " );
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if(!skipList.contains(name))
			buffer.append("\""+name+"\" : ");
	}
 	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if(!skipList.contains(name))
			buffer.append(", ");
	}
  	public String toString() {
 		return buffer.toString();
	}
} 
