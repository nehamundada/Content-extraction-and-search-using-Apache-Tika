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
	public static final String HELLO_MIME_TYPE = "application/tsv";
	
	private static final long serialVersionUID = -6656102320836888910L;

    private static final ServiceLoader LOADER =
            new ServiceLoader(TSVParser.class.getClassLoader());

	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	public void parse(
			InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context)
					throws IOException, SAXException, TikaException {

//		metadata.set(Metadata.CONTENT_TYPE, HELLO_MIME_TYPE);
//		metadata.set("Hello", "World");

		AutoDetectReader reader = new AutoDetectReader(
                new CloseShieldInputStream(stream), metadata,
                context.get(ServiceLoader.class, LOADER));	

		CSVReader csvReader = null;
		try {
			
			
			Charset charset = reader.getCharset();
			MediaType type = new MediaType(MediaType.TEXT_PLAIN, charset);
			metadata.set(Metadata.CONTENT_TYPE, type.toString());
			// deprecated, see TIKA-431
			metadata.set(Metadata.CONTENT_ENCODING, charset.name());

			XHTMLContentHandler xhtml =
					new XHTMLContentHandler(handler, metadata);
			xhtml.startDocument();
			xhtml.startElement("table");
			
			
//			csvReader = new CSVReader(new InputStreamReader(stream), '\t');
			csvReader = new CSVReader(reader, '\t');
		    String [] nextLine;
		    while ((nextLine = csvReader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	xhtml.startElement("tr");
		    	for(String i : nextLine) {
		    		xhtml.element("td", i);
		    	}
//		        System.out.println(nextLine[0] + nextLine[1] + "etc...");
		    	xhtml.endElement("tr");
		    }
			xhtml.endElement("table");
			xhtml.endDocument();
		} finally {
			csvReader.close();
		}



		XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
		xhtml.startDocument();
		xhtml.endDocument();
	}
}