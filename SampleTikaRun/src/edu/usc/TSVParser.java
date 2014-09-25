package edu.usc;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;

public class TSVParser extends AbstractParser {

	private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
	public static final String APPLICATION_MIME_TYPE = "application/tsv";
	
	private static final long serialVersionUID = -6656102320836888910L;

 	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	public void parse(
			InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context)
					throws IOException, SAXException, TikaException {

 
		InputStreamReader reader = new InputStreamReader(stream);	

		CSVReader csvReader = null;
		try {
			
  			metadata.set(Metadata.CONTENT_TYPE, APPLICATION_MIME_TYPE );
			// deprecated, see TIKA-431
			metadata.set(Metadata.CONTENT_ENCODING, reader.getEncoding() );

			XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
  			csvReader = new CSVReader(reader, '\t');

			 String [] nextLine;
			 String [] headers = {
			    					"postedDate",
			                        "Location",
			                        "department",
			                        "Title ",
			                        "",
			                        "salary",
			                        "start",
			                        "duration",
			                        "jobtype",
			                        "applications",
			                        "company",
			                        "contactPerson",
			                        "phoneNumber",
			                        "faxNumber",
			                        "Location",
			                        "latitude",
			                        "longitude",
			                        "firstSeenDate",
			                        "url",
			                        "lastSeenDate"
			                        };

	
			    while ((nextLine = csvReader.readNext()) != null) {
	 				xhtml.startDocument();

			        // nextLine[] is an array of values from the line
			    	for(int i=0; i<nextLine.length; i++) {
			    		if(i== 4){
				    		continue;
				    	}
				    	//xhtml.startElement(headers[i]);
				    	xhtml.element(headers[i], nextLine[i]);
			    		//xhtml.endElement(headers[i]);
			    	}
		 			xhtml.endDocument();

  			    }
		} finally {
			csvReader.close();
		}
   
	}
}