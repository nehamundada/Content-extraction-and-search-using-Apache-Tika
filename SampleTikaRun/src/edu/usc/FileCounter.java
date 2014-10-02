package edu.usc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Map;

public class FileCounter {

	public static void main(String[] args) throws Exception {
		
		int totalCount = 0;
		File f = new File("F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/JSONDATA");
		File files [] = f.listFiles();
 		
		int ctr = 0;
		String line = null;
		for (File file : files) {
			File countfile = new File(file+"/count.txt");
			BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream(countfile)));
			while(( line=reader.readLine() )!=null){
				int count = Integer.parseInt(line);
				totalCount = totalCount + count;
				System.out.println(file + " : " + count);
			}
			reader.close();
			ctr++;
 		}
		System.out.println(" ****** TOTAL FILES ****** " + ctr);
		System.out.println(" ************ TOTAL COUNT ********** : " + totalCount);
		
	}
}
