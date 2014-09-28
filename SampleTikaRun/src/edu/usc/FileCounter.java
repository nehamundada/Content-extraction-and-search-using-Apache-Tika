package edu.usc;

import java.io.File;

public class FileCounter {

	public static void main(String[] args) {
		
		int totalCount = 0;
		File f = new File("F:/COURSES/Sem3/CSCI_572_Information Retreival and Search Engines/Assignment1/JSON");
		File files [] = f.listFiles();
		
		int ctr = 1;
		for (File file : files) {
			int count = file.listFiles().length;
			totalCount = totalCount + count;
			System.out.println( ctr + "	, 	"+ file.getName()+ " 	, Count , " + count );
			ctr ++ ;
		}
		System.out.println(" ************ TOTAL : " + totalCount);
	}
}
