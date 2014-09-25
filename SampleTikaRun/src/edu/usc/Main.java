package edu.usc;

import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
 
public class Main {
	
	public static void main(String args[]) {
 		
		try {
 			InputStream input = new FileInputStream("sample.tsv");
	        ContentHandler handler = new JSONTableContentHandler();
	        Metadata metadata = new Metadata();
 	        new TSVParser().parse(input, handler, metadata, new ParseContext());
 	        
 	       /* String plainText = handler.toString();
 	        System.out.println(plainText);*/
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
 	}

}
