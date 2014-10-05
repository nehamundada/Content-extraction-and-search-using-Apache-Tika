package edu.usc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
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

public class TSVParser extends AbstractParser {

	private String filename = "";
	private String filePath = "";
	private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
	private static final String APPLICATION_MIME_TYPE = "application/tsv";

	private static String OUTPUT_DIRECTORY;

	private static final long serialVersionUID = -6656102320836888910L;
	
	private boolean generateJSON = false;
	private boolean enableDeDup = false;
	
	Map <Integer,Integer> map = new HashMap <Integer,Integer>();
//	Map <String,Integer> map = new HashMap <String,Integer>();
	
	/**
	 * @param outputDir
	 * @param isJSON
	 * @param fName
	 * @param fPath
	 * 
	 * */
	public TSVParser(String outputDir, boolean isJSON, boolean deDup) {
		super();
		OUTPUT_DIRECTORY = outputDir;
		this.generateJSON = isJSON;
		this.enableDeDup = deDup;
	}
	
	public TSVParser() {
		super();
	}
	
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

		InputStreamReader reader = new InputStreamReader(stream);	
		
		

		CSVReader csvReader = null;
		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			String fileNameOnly = filename.substring(0, filename.length()-4);
			String folderName = OUTPUT_DIRECTORY+"/"+fileNameOnly;
//			new File(folderName).mkdir();

			metadata.set(Metadata.CONTENT_TYPE, APPLICATION_MIME_TYPE );
			metadata.set(Metadata.CONTENT_ENCODING, reader.getEncoding() );

			csvReader = new CSVReader(reader, '\t');

			String [] nextLine;
			String [] headers = {
					"postedDate", "Location", "department", "Title ", "", "salary",  "start", "duration", "jobtype", "applications", "company",
					"contactPerson", "phoneNumber", "faxNumber", "Location", "latitude", "longitude", "firstSeenDate", "url", "lastSeenDate"  
			};
			int count = 0;
			int deDup = 0;
			int uniqCount = 0;
			while ((nextLine = csvReader.readNext()) != null) {
				XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
				xhtml.startDocument();

				// nextLine[] is an array of values from the line
				for(int i=0; i<3; i++) {
					xhtml.element(headers[i], nextLine[i]);
				}
				for(int i=3; i<nextLine.length; i++) {
					xhtml.element(headers[i], nextLine[i]);
				}
				xhtml.endDocument();

				
				JSONTableContentHandler newHandler = (JSONTableContentHandler) handler;
				
				// if deduplication is enabled, we check the map for unique hashCode
				boolean isUniqRow = true;
				if(this.enableDeDup) {
//					String hsh = byteArrayToHexString(md.digest(newHandler.uniqueString.toString().getBytes()));
//					System.out.println(newHandler.uniqueString.toString());
					
//					if( map.containsKey(hsh)){
//						isUniqRow = false;
//						deDup++;
//					} else {
//						map.put(hsh , 1 );
//						uniqCount++;
//					}
							
					if( map.containsKey( newHandler.uniqueString.toString().hashCode() )){
						isUniqRow = false;
						deDup++;
					} else {
						map.put( newHandler.uniqueString.toString().hashCode() , 1 );
						uniqCount++;
					}
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
				count ++;
 			}
			
			if(this.enableDeDup) {
				System.out.println(fileNameOnly + " : " + count + " Uniq: " + uniqCount + " Dup: " + deDup);
			} else {
				System.out.println(fileNameOnly + " : " + count);
			}
			
//			File file = new File(folderName+"/count.txt");
//			BufferedWriter output = new BufferedWriter(new FileWriter(file));
//			output.write(  String.valueOf(count-1) );
//			output.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csvReader.close();
		}

	}
	
	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
}