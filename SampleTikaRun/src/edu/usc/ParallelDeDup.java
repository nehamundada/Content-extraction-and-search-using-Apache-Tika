package edu.usc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

public class ParallelDeDup extends Thread {
	
	private static HashSet<UniqueJob> list = new HashSet<UniqueJob>();
	private static Set<UniqueJob> synList = Collections.synchronizedSet(list);
	private UniqueJob toCompare = null;
	private int threadNum = 0;
	
	public ParallelDeDup(UniqueJob j, int i) {
		this.toCompare = j;
		this.threadNum = i;
	}
	
	@Override
	public void run() {

//		System.out.println("Starting :" + this.threadNum);
		boolean isUnique = true;
		if(synList.size() < 1) {
			System.out.println("Adding the first object");
			synList.add(this.toCompare);
			return;
		}
		
		synchronized (synList) {
			Iterator<UniqueJob> iterator = synList.iterator();
			while (iterator.hasNext()) {
				if(!compareObjects(iterator.next(),this.toCompare)) {
					isUnique = false;
					break;
				}
			}
		}
		if(isUnique) {
			synList.add(this.toCompare);
		}
//		System.out.println("End :" + this.threadNum);
		if(this.threadNum % 1000 == 0){
			System.out.println("***** "+this.threadNum+" ********   Uniq : " + synList.size());
		}
	}
	
	private double compareStrings(String stringA, String stringB) {
		JaroWinkler algorithm = new JaroWinkler();
		return algorithm.getSimilarity(stringA, stringB);
	}
	
	private Boolean compareObjects(UniqueJob uniqueJob1, UniqueJob uniqueJob2) {
		float threshold = 0.93f;
		
		if(compareStrings(uniqueJob1.title,uniqueJob2.title) < threshold
				&& compareStrings(uniqueJob1.location,uniqueJob2.location) < threshold
				&& compareStrings(uniqueJob1.department,uniqueJob2.department) < threshold
				&& compareStrings(uniqueJob1.jobtype,uniqueJob2.jobtype) < threshold
				&& compareStrings(uniqueJob1.duration,uniqueJob2.duration) < threshold
				&& compareStrings(uniqueJob1.company,uniqueJob2.company) < threshold) {
			return false;
		}
		return true;
	}
	
	
	
	
	

}
