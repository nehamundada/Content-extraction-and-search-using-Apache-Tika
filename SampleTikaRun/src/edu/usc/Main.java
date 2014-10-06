package edu.usc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
  
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;

 
public class Main {

	final static String DATA_DIRECTORY = "/Volumes/SHRI/Data";
	//final static String DATA_DIRECTORY = "F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/employment/";
	
	final static String OUTPUT_DIRECTORY = "";
	
	
	public static void main(String args[]) {
		
		runParser(true, false);
		return;
		
//		
//		try {
//			
//			Metadata metadata = new Metadata();
//			TSVParser parser = new TSVParser("",false,true);
//
//			File f = new File(DATA_DIRECTORY);
//			File files [] = f.listFiles();
//			
//			int numberOfFiles = files.length ;
//			
//			for (int i=0;i < numberOfFiles; i++) {
//				ContentHandler handler = new JSONTableContentHandler();
//				System.out.println(" Processing : " + i);
//				File file = files[i];
//				InputStream input = new FileInputStream(file.getAbsolutePath());
//				parser.setFilename(file.getName());
//				parser.setFilePath(file.getAbsolutePath());
//				parser.parse(input, handler, metadata, new ParseContext());
//			}
//
////			System.out.println(" TOTAL JOBS : " + parser.count);
////			System.out.println(" DUPLICATE JOBS : " + ( parser.count- parser.map.size() ) );
//			System.out.println(" UNIQUE JOBS : " + parser.map.size());
//			System.out.println(" Writing data");
//
////			 File file = new File ("F:/output_4GB.tsv");
////			 PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
//			 
//			// int size = parser.writeList.size();
//			 //System.out.println("WRITING DATA" + size);
//
//		/*	 for (int i = 0; i < size; i++) {
//			 	printWriter.println(parser.writeList.get(i));
//			 }*/
////			 printWriter.close (); 
//			 System.out.println(" DONE ...");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
	
	
	
	private static void runParser(boolean performDeDup, boolean createJSONFiles) {

		try {
			TSVParserDeduplication parser = new TSVParserDeduplication();
			
			parser.setOUTPUT_DIRECTORY(OUTPUT_DIRECTORY);
			parser.setEnableDeDup(performDeDup);
			parser.setGenerateJSON(createJSONFiles);
			
			ContentHandler handler = new JSONTableContentHandler();
			Metadata metadata = new Metadata();

			File f = new File(DATA_DIRECTORY);
			File files [] = f.listFiles();

			System.out.println("filename\tTotal\tUniq\tDup\tMapSize");
			for (int i=0; i<files.length; i++) {
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
