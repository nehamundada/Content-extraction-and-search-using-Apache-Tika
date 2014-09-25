package edu.usc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class Main {
	
	public static void main(String args[]) {
		Parser parser = new HelloParser();
		
		try {
//			InputStream input = new FileInputStream("sample.html");
			InputStream input = new FileInputStream("computrabajo-ar-20121106.tsv");
	        ContentHandler handler = new BodyContentHandler(-1);
	        Metadata metadata = new Metadata();
//	        new HtmlParser().parse(input, handler, metadata, new ParseContext());
	        new TSVParser().parse(input, handler, metadata, new ParseContext());
	        String plainText = handler.toString();
	        
	        System.out.println(plainText);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		parser.parse(new FileInputStream("sample.tsv"), handler, metadata, context);
	}

}
