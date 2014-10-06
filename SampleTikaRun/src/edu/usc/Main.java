package edu.usc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;

 
public class Main {
	
	private static Properties prop = new Properties();
	
	private enum PropKeys {
		generateJSON,
		enableDeDup,
		inputDataDir,
		outputDataDir
	}
	
	public Main() {
		try {
			File file = new File("App.properties");
			FileInputStream fileInput = new FileInputStream(file);
			prop = new Properties();
			prop.load(fileInput);
			fileInput.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Main objMain = new Main();
		objMain.runParser(Boolean.parseBoolean(prop.getProperty(PropKeys.enableDeDup.name())), 
				Boolean.parseBoolean(prop.getProperty(PropKeys.generateJSON.name())));
	}
	
	
	/**
	 * @param performDeDup Boolean : Whether to have deduplication or now
	 * @param createJSONFiles Boolean: if we have to create the json files or not 
	 */
	private void runParser(boolean performDeDup, boolean createJSONFiles) {

		try {
			TSVParser parser = new TSVParser();
			
			TSVParser.setOUTPUT_DIRECTORY(prop.getProperty(PropKeys.outputDataDir.name()));
			parser.setEnableDeDup(performDeDup);
			parser.setGenerateJSON(createJSONFiles);
			
			ContentHandler handler = new JSONTableContentHandler();
			Metadata metadata = new Metadata();

			File f = new File(prop.getProperty(PropKeys.inputDataDir.name()));
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
