package com.kruton.geocoder.helpers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.kruton.geocoder.beans.LocationBean;

public class CSVParser {


	public ArrayList<LocationBean> parseLatLonCSV(ArrayList<LocationBean> locations, String filename) {
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filename));

		    String [] nextLine;
		    nextLine = reader.readNext();
		    System.out.println(nextLine[0] + nextLine[1]);
		    if (!(nextLine[0].toLowerCase().equals("latitude") || nextLine[1].toLowerCase().equals("longitude")))
		    	System.out.println("First line was not latitude and longitude - check format and retry");
		    else {
			    while ((nextLine = reader.readNext()) != null) {
			        // nextLine[] is an array of values from the line
			        //System.out.println(nextLine[0] + nextLine[1]);
			        locations.add(new LocationBean(Double.valueOf(nextLine[0]), Double.valueOf(nextLine[1])));
			    }
		    }
		    
		    reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	    return locations;
	}
	
	public ArrayList<LocationBean> parseNameAddressCSV(ArrayList<LocationBean> locations, String filename) {
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filename));

		    String [] nextLine;
		    nextLine = reader.readNext();
		    System.out.println(nextLine[0] + nextLine[1]);
		    if (!(nextLine[0].toLowerCase().equals("name") || nextLine[1].toLowerCase().equals("address")))
		    	System.out.println("First line was not name and address - check format and retry");
		    else {
			    while ((nextLine = reader.readNext()) != null) {
			        // nextLine[] is an array of values from the line
			        //System.out.println(nextLine[0] + nextLine[1]);
			        locations.add(new LocationBean(nextLine[0], nextLine[1]));
			    }
		    }
		    
		    reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	    return locations;
	}
	
	public void writeCSV(ArrayList<LocationBean> locations, String filename) {
		CSVWriter writer;
		
		try {
			writer = new CSVWriter(new FileWriter(filename));
		     // feed in your array (or convert your data to an array)
		     //String[] entries = "first#second#third".split("#");
		     //writer.writeNext(entries);
		     
		    for(LocationBean location : locations) {
		    	writer.writeNext(location.getStringArray());
		    }
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
