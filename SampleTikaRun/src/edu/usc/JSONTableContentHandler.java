package edu.usc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class JSONTableContentHandler extends ContentHandlerDecorator {

	final List <String> skipList = new ArrayList<String>( Arrays.asList("html", "head", "meta", "title", "body") );

	final List uniqueList = new ArrayList<String> ( Arrays.asList("Location", "department", "duration" , "Title", "jobtype", "company"));

	String elementName = "";

	JSONObject json = null;

	public StringBuffer uniqueString = null;
	public String returnString = "";
	
	public void startDocument() throws SAXException {
		json = new JSONObject();
		uniqueString = new StringBuffer();
	}

	public void endDocument() throws SAXException {
		returnString = uniqueString.toString();//.toLowerCase();
	}
	public void characters(char[] ch, int start, int length) throws SAXException {
		String val = new String(ch);
		
		if(val.length() > 0 && !skipList.contains(val)){
			try{
				json.put(elementName, new String(val));
				
				if( uniqueList.contains(elementName) ){
					 uniqueString.append(new String(val).toLowerCase()) ;
				} 
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if(!skipList.contains(name)){
			elementName = name;
		}
	}
	public void endElement(String uri, String localName, String name) throws SAXException {
	}
	public String toString() {
		return json.toString();
	}
} 
