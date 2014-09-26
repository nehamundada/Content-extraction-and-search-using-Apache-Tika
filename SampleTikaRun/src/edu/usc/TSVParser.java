package edu.usc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
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

	private static final String OUTPUT_DIRECTORY = "F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/JSON";

	private static final long serialVersionUID = -6656102320836888910L;

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

			String fileNameOnly = filename.substring(0, filename.length()-4);
			String folderName = OUTPUT_DIRECTORY+"/"+fileNameOnly;
			new File(folderName).mkdir();
			
			metadata.set(Metadata.CONTENT_TYPE, APPLICATION_MIME_TYPE );
			metadata.set(Metadata.CONTENT_ENCODING, reader.getEncoding() );

			csvReader = new CSVReader(reader, '\t');

			String [] nextLine;
			String [] headers = {
					"postedDate", "Location", "department", "Title ", "", "salary",  "start", "duration", "jobtype", "applications", "company",
					"contactPerson", "phoneNumber", "faxNumber", "Location", "latitude", "longitude", "firstSeenDate", "url", "lastSeenDate"  
			};
			int count = 1;
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
				
				String outputFileName = fileNameOnly +"_"+ count+".json";
				//System.out.println(outputFileName);
				
 				File file = new File(folderName+"/"+outputFileName);
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
				output.write(handler.toString());
				output.close();
				count ++;
			}
		} finally {
			csvReader.close();
		}

	}
}