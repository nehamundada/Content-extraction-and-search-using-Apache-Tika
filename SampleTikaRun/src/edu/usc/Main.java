package edu.usc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;

public class Main {

//	final static String DATA_DIRECTORY = "F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/Data";
//	final static String OUTPUT_DIRECTORY = "F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/JSONDATA";
	final static String DATA_DIRECTORY = "/Volumes/SHRI/Data";
	final static String OUTPUT_DIRECTORY = "";

	
	public static void main(String args[]) {

		runParser(true, false);
	}

	private static void runParser(boolean performDeDup, boolean createJSONFiles) {

		try {
			TSVParser parser = new TSVParser(OUTPUT_DIRECTORY,createJSONFiles, performDeDup);
			ContentHandler handler = new JSONTableContentHandler();
			Metadata metadata = new Metadata();

			File f = new File(DATA_DIRECTORY);
			File files [] = f.listFiles();

			for (int i=0;i<430;i++) {
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
