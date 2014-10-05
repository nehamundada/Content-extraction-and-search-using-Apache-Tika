package edu.usc;

public class UniqueJob {
	String location;
	String department;
	String duration;
	String title;
	String jobtype; 
	String company;
	public UniqueJob(String location, String department, String duration,
			String title,String jobtype,String company) {
		this.location = location;
		this.department = department;
		this.duration = duration;
		this.title = title;
		this.jobtype = jobtype;
		this.company = company;
	}
}