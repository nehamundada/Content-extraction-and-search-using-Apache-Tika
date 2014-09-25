package edu.usc;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
  
public class JSONTableContentHandler extends ContentHandlerDecorator {

	StringBuffer buffer = new StringBuffer();
	 public JSONTableContentHandler() {
   	 }
 	 
 	public void startDocument() throws SAXException {
 		buffer.append("{");
 	}
 	
	public void endDocument() throws SAXException {
		buffer.append("}");
		System.out.println( buffer.toString() );
 	}
 	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch);
	}
 	 
  	public void startElement(String uri, String localName, String name,
 			Attributes atts) throws SAXException {
  		buffer.append("\""+name+"\" : ");
 	}
  	@Override
  	public void endElement(String uri, String localName, String name)
  			throws SAXException {
  		buffer.append(",");
  	}
   /*	public String toString() {
  		return buffer.toString();
 	}
	 */
	 
} 
