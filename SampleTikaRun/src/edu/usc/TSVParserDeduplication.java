package edu.usc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;

public class TSVParserDeduplication extends AbstractParser {

	private String filename = "";
	private String filePath = "";
	private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
	private static final String APPLICATION_MIME_TYPE = "application/tsv";

	private static String OUTPUT_DIRECTORY = "";
	private boolean generateJSON = false;
	private boolean enableDeDup = false;

	public boolean isGenerateJSON() {
		return generateJSON;
	}
	public void setGenerateJSON(boolean generateJSON) {
		this.generateJSON = generateJSON;
	}
	public boolean isEnableDeDup() {
		return enableDeDup;
	}
	public void setEnableDeDup(boolean enableDeDup) {
		this.enableDeDup = enableDeDup;
	}
	public static String getOUTPUT_DIRECTORY() {
		return OUTPUT_DIRECTORY;
	}
	public static void setOUTPUT_DIRECTORY(String oUTPUT_DIRECTORY) {
		OUTPUT_DIRECTORY = oUTPUT_DIRECTORY;
	}

	private static final long serialVersionUID = -6656102320836888910L;

	public Map <Integer,Integer> map = new HashMap <Integer,Integer>();
	
     public int count = 0;
	
    List <String> writeList = new ArrayList<String>();
    
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return filePath;
	} 
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	public void parse( InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context)
			throws IOException, SAXException, TikaException {

		BufferedReader reader = new BufferedReader( new InputStreamReader(stream));	

		CSVReader csvReader = null;
		int uniqCount = 0;
		int dupCount = 0;
		try {

			String fileNameOnly = filename.substring(0, filename.length()-4);
			String folderName = OUTPUT_DIRECTORY+"/"+fileNameOnly;
//			new File(folderName).mkdir();

			metadata.set(Metadata.CONTENT_TYPE, APPLICATION_MIME_TYPE );
			//metadata.set(Metadata.CONTENT_ENCODING, reader.getEncoding() );

			csvReader = new CSVReader(reader, '\t');
			 			
			String [] nextLine;
			String [] headers = {
					"postedDate", "Location", "department", "Title", "", "salary",  "start", "duration", "jobtype", "applications", "company",
					"contactPerson", "phoneNumber", "faxNumber", "Location", "latitude", "longitude", "firstSeenDate", "url", "lastSeenDate"  
			};
			
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				nextLine = line.split("\t");
				XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
				xhtml.startDocument();
				 count ++;
 				// nextLine[] is an array of values from the line
				for(int i=0; i<4; i++) {
					xhtml.element(headers[i], nextLine[i]);
 				}
				for(int i=5; i<nextLine.length; i++) {
					xhtml.element(headers[i], nextLine[i]);
 				}
				xhtml.endDocument();

				JSONTableContentHandler newHandler = (JSONTableContentHandler) handler;
				
				boolean isUniqRow = false;
				if(this.enableDeDup) {
				
					if( !map.containsKey( newHandler.returnString.hashCode() )) {					
						map.put( newHandler.returnString.hashCode() , new Integer(1) );
						isUniqRow = true;
						uniqCount++;
 
						writeList.add(line);
					} else {
						dupCount++;
					}
					
					//String outputFileName = fileNameOnly +"_"+ count+".json";
					//  System.out.println(handler.toString());
					//System.out.println(outputFileName);

					//File file = new File(folderName+"/"+outputFileName);
					//BufferedWriter output = new BufferedWriter(new FileWriter(file));
					//output.write(handler.toString());
					//output.close();
					
				}
				if(isUniqRow) {
					
					if(this.generateJSON) {
						new File(folderName).mkdir();
						
						String outputFileName = fileNameOnly +"_"+ count+".json";
						System.out.println(handler.toString());
						System.out.println(outputFileName);
	
						File file = new File(folderName+"/"+outputFileName);
						BufferedWriter output = new BufferedWriter(new FileWriter(file));
						output.write(handler.toString());
						output.close();
					}
				}
			}
			if(this.enableDeDup) {
				System.out.println(fileNameOnly + "\t" + count + "\t" + uniqCount + "\t" + dupCount + "\t" + map.size());
			} else {
				System.out.println(fileNameOnly + " : " + count);
			}
			
		} finally {
			csvReader.close();
		}

	}
}