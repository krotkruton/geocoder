package com.kruton.geocoder.beans;

import java.text.DecimalFormat;


public class LocationBean implements Comparable<LocationBean>{
	private String name;
	private String address;
	private Double latitude = 0.0;
	private Double longitude = 0.0;
	private Double weight = 0.0;

	public LocationBean() {

	}
	
	public LocationBean(String name, String address) {
		this.setName(name);
		this.address = address;
	}
	
	
	public LocationBean(Double lat, Double lon) {
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public LocationBean(Double lat, Double lon, Double weight) {
		this.latitude = lat;
		this.longitude = lon;
		this.weight = weight;
	}
	
	public LocationBean(String name, Double lat, Double lon) {
		this.name = name;
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public LocationBean(String name, Double lat, Double lon, Double weight) {
		this.name = name;
		this.latitude = lat;
		this.longitude = lon;
		this.weight = weight;
	}
	

	public int compareTo(LocationBean otherLocation) {
    	
    	int xComp = Double.compare(this.getLatitude(), otherLocation.getLatitude());
        if(xComp == 0.0)
            return Double.compare(this.getLongitude(), otherLocation.getLongitude());
        else
            return xComp;
	}
	
	
	
	/*
	 * Specialty Getters and Setters
	 */

	public int getRoundedWeight() {
		return (int) Math.round(weight);
	}
	
	public String[] getStringArray() {
		return new String[]{name, address, latitude.toString(), longitude.toString()}; 
	}
	
	// Google requires lat / lon pairs to be in the form 41.43206,-81.38992 (no spaces) for the Distance Matrix
	public String getGoogleLatLon() {
		//DecimalFormat df = new DecimalFormat("#.00000");
		//return df.format(this.latitude) + "," + df.format(this.longitude);
		
		return this.latitude + "," + this.longitude;
	}

	
	
	/*
	 * Standard Getters and Setters
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	
	/*
	 * Print methods
	 */
	public void printLatLonWeight() {
		DecimalFormat df = new DecimalFormat("#.00000");
		printLatLonWeight(df);
	}
	
	public void printLatLonWeight(DecimalFormat df) {
		System.out.println(df.format(this.latitude) + " " + df.format(this.longitude) + " " + this.weight);
	}
	
	public void printLatLonWeightAsTime() {
		DecimalFormat df = new DecimalFormat("#.00000");
		int hours = (int) Math.floor(this.weight / 3600);
		int minutes = (int) Math.floor((this.weight % 3600) / 60);
		int seconds = (int) Math.floor((this.weight % 60 ));
		
		System.out.println(df.format(this.latitude) + " " + df.format(this.longitude) + " " + hours + ":" + minutes + ":" + seconds);
	}
}
