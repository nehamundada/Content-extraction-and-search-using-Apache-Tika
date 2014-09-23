package edu.usc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class HelloParser extends AbstractParser {

        private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("hello"));
        public static final String HELLO_MIME_TYPE = "application/hello";
        
        public Set<MediaType> getSupportedTypes(ParseContext context) {
                return SUPPORTED_TYPES;
        }

        public void parse(
                        InputStream stream, ContentHandler handler,
                        Metadata metadata, ParseContext context)
                        throws IOException, SAXException, TikaException {

                metadata.set(Metadata.CONTENT_TYPE, HELLO_MIME_TYPE);
                metadata.set("Hello", "World");

                XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
                xhtml.startDocument();
                xhtml.endDocument();
        }
}