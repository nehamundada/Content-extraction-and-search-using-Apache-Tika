package edu.usc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
 
public class Main {
	
	public static void main(String args[]) {
 		
		try {
 			
	        ContentHandler handler = new JSONTableContentHandler();
	        Metadata metadata = new Metadata();
 	        TSVParser parser = new TSVParser();
 	       
 	        File f = new File("F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/Data");
 	        File files [] = f.listFiles();
 	      
 	        for (int i=0;i<100;i++) {
 	        	 File file = files[i];
 	        	 InputStream input = new FileInputStream(file.getAbsolutePath());
 	        	 parser.setFilename(file.getName());
 	        	 parser.setFilePath(file.getAbsolutePath());
 	 	         parser.parse(input, handler, metadata, new ParseContext());
			}
  	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
 	}

}
