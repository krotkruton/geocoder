package com.kruton.geocoder.helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kruton.geocoder.beans.LocationBean;
import com.kruton.geocoder.utils.Debug;
import com.kruton.geocoder.utils.Debug.LEVEL;

public class ContourHelper {
	List<LocationBean> locations = new ArrayList<LocationBean>();
	
	public ContourHelper() {
		
	}
	
	public ContourHelper(ArrayList<LocationBean> locations) {
		this.locations = locations;
		
	}


	public void sortLocations() {
		Collections.sort(locations);
	}
	
	
	
	/*
	 * Convenience methods
	 */
	public LocationBean[][] fillInArray(LocationBean[][] locationArray) {
		return fillInArray(locationArray, Debug.LEVEL.NONE);
	}
	
	
	
	
	
	/*
	 * Action Methods
	 */
	
	
	public Map<Integer, List<LocationBean>> groupLocationsByWeight(LocationBean[][] locationArray) {
		Map<Integer, List<LocationBean>> groupings = new HashMap<Integer, List<LocationBean>>();
		int rows = locationArray[0].length;
		int cols = locationArray.length;

		
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				int weight = locationArray[x][y].getRoundedWeight();
				
				/*
				List<LocationBean> weightGroup = groupings.get(weight);
				
				if (weightGroup == null) {
					weightGroup = new ArrayList<LocationBean>();
					System.out.println("Creating new grouping for weight: " + weight);
				}

				weightGroup.add(locationArray[x][y]);
				*/
				
				if (!groupings.containsKey(weight)) {
					System.out.println("Creating new grouping for weight: " + weight);
					groupings.put(weight, new ArrayList<LocationBean>());
					
				}
				groupings.get(weight).add(locationArray[x][y]);
			}
		}
		
		return groupings;
	}
	
	
	
	public LocationBean[][] createLocationArrayFromList(int rows, int cols) {
		
		LocationBean[][] locationArray = new LocationBean[cols][rows];
		
		int count = 0;
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				System.out.println(y + " " + x);
				locationArray[x][y] = locations.get(count);
				count++;
			}
		}
		
		return locationArray;
	}
	
	public LocationBean[][] expandArray(LocationBean[][] initialLocationArray) {
		int rows = initialLocationArray[0].length;
		int cols = initialLocationArray.length;
		
		LocationBean[][] newLocationArray = new LocationBean[cols * 2 - 1][rows * 2 - 1];
		
		int newRowLoc = 0;
		int newColLoc = 0;
		newLocationArray[newColLoc][newRowLoc] = initialLocationArray[0][0];
		
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				if (y == 0)
					newRowLoc = 0;
				else
					newRowLoc = y * 2;
				if (x == 0)
					newColLoc = 0;
				else
					newColLoc = x * 2;
				
				newLocationArray[newColLoc][newRowLoc] = initialLocationArray[x][y];

			}
		}
		
		return newLocationArray;
	}
	
	
	public LocationBean[][] fillInArray(LocationBean[][] locationArray, LEVEL debug) {
		double xStep = 0.0;
		double yStep = 0.0;
		
		double lat1 = locationArray[0][0].getLatitude();
		double lat2 = locationArray[0][2].getLatitude();
		double lon1 = locationArray[0][0].getLongitude();
		double lon2 = locationArray[2][0].getLongitude();
		xStep = (lat2 - lat1) / 2;
		yStep = (lon2 - lon1) / 2;
		
		System.out.println("Initial lats: " + lat1 + " " + lat2 + " | step = " + xStep);
		System.out.println("Initial lons: " + lon1 + " " + lon2 + " | step = " + yStep);
		
		
		
		locationArray = firstPass(locationArray, debug);
		
		if (debug.getLevel() >= 2) System.out.println("After first pass");
		if (debug.getLevel() >= 2) printLocationWeightsAsMap(locationArray);
		
		locationArray = secondPass(locationArray, debug, xStep, yStep);
		
		return locationArray;
	}
	
	public LocationBean[][] firstPass(LocationBean[][] locationArray, LEVEL debug) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;

		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				double sum = 0.0;
				if (debug.getLevel() >= 3) System.out.print(x + " " + y);
				if (locationArray[y][x] == null 
						&& 0 < y && y < cols - 1				//make sure it's not at an edge
						&& 0 < x && x < rows - 1
						&& locationArray[y-1][x] == null  		//make sure it's not "in between" two other items but is instead the center of an X
						&& locationArray[y-1][x-1] != null  	//make sure that all four points have values
						&& locationArray[y-1][x-1] != null
						&& locationArray[y-1][x-1] != null
						&& locationArray[y-1][x-1] != null) {
					sum += locationArray[y-1][x-1].getWeight()
						+ locationArray[y+1][x-1].getWeight()
						+ locationArray[y-1][x+1].getWeight()
						+ locationArray[y+1][x+1].getWeight();
					
					if (debug.getLevel() >= 3) System.out.print(sum);
					double average = sum / 4;
					double lat = (locationArray[y+1][x+1].getLatitude() + locationArray[y-1][x-1].getLatitude()) / 2;
					double lon = (locationArray[y+1][x+1].getLongitude() + locationArray[y-1][x-1].getLongitude()) / 2;
					locationArray[y][x] = new LocationBean(lat,lon,average);
				}
				if (debug.getLevel() >= 3) System.out.println();

			}
		}
		
		return locationArray;
	}

	public LocationBean[][] secondPass(LocationBean[][] locationArray, LEVEL debug, double lonStep, double latStep) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;

		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				double sum = 0.0;
				int count = 0;
				if (debug.getLevel() >= 3) System.out.print(x + " " + y);
				if (locationArray[y][x] == null) {
					if (y > 0) {
						if (debug.getLevel() >= 4) System.out.print("a");
						sum += locationArray[y-1][x].getWeight();
						count++;
					}
					if (x > 0) {
						if (debug.getLevel() >= 4) System.out.print("b");
						sum += locationArray[y][x-1].getWeight();
						count++;
					}
					if (y < cols - 1) {
						if (debug.getLevel() >= 4) System.out.print("c");
						sum += locationArray[y+1][x].getWeight();
						count++;
					}
					if (x < rows - 1) {
						if (debug.getLevel() >= 4) System.out.print("d");
						sum += locationArray[y][x+1].getWeight();
						count++;
					}
					
					double average = sum / count;
					
					double lat = 0.0;
					double lon = 0.0;
					
					if (y == 0 || y == cols - 1) {
						lon = locationArray[y][x-1].getLongitude();
					} else {
						lon = (locationArray[y-1][x].getLongitude() + locationArray[y+1][x].getLongitude()) / 2;
					}
					if (x == 0 || x == rows - 1) {
						lat = locationArray[y-1][x].getLatitude();
					} else {
						lat = (locationArray[y][x-1].getLatitude() + locationArray[y][x+1].getLatitude()) / 2;
					}

					locationArray[y][x] = new LocationBean(lat,lon,average);
				}
				if (debug.getLevel() >= 3) System.out.println();

			}
		}
		
		return locationArray;
	}

	
	
	/*
	 * Getters, Setters, and Printers
	 * 
	 */

	public List<LocationBean> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationBean> locations) {
		this.locations = locations;
	}
	
	public void printLocationWeightsAsMap(LocationBean[][] locationArray) {
		DecimalFormat df = new DecimalFormat("#.00");
		
		printLocationWeightsAsMap(locationArray, df);
	}

	public void printLatLonsAsMap(LocationBean[][] locationArray) {
		DecimalFormat df = new DecimalFormat("#.00");
		printLatLonsAsMap(locationArray, df);
	}

	public void printLatLonsWeightsAsMap(LocationBean[][] locationArray) {
		DecimalFormat df = new DecimalFormat("#.00");
		printLatsLonsWeightsAsMap(locationArray, df);
	}
	
	
	public void printLocationsWithWeights(LocationBean[][] locationArray) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;

		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				System.out.println(locationArray[x][y].getLatitude() + ", " + locationArray[x][y].getLongitude() + ", " + locationArray[x][y].getWeight());
			}
		}
	}
	
	public void printLatLonsAsMap(LocationBean[][] locationArray, DecimalFormat df) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;
		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				System.out.print("(" + locationArray[x][y].getLatitude() + ", " + locationArray[x][y].getLongitude() + ") ");

			}
			System.out.println();
		}
	}
	
	public void printLocationWeightsAsMap(LocationBean[][] locationArray, DecimalFormat df) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;
		
		
		System.out.println("-------- Map of size [" + rows + "," + cols + "] ----------");
		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				
				if (locationArray[x][y] != null)
					System.out.print(df.format(locationArray[x][y].getWeight()) + " ");
				else 
					System.out.print("  x   ");
			}
			System.out.println();
		}
	}
	
	public void printLatsLonsWeightsAsMap(LocationBean[][] locationArray, DecimalFormat df) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;
		
		
		System.out.println("-------- Map of size [" + rows + "," + cols + "] ----------");
		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				
				if (locationArray[x][y] != null)
					System.out.print("(" + locationArray[x][y].getLatitude() + ", " + locationArray[x][y].getLongitude() + ", " + df.format(locationArray[x][y].getWeight()) + ") ");
				else 
					System.out.print("  x   ");
			}
			System.out.println();
		}
	}
	
	// very innefficient, but works well enough for simple testing
	public void printGrouping(List<LocationBean> grouping, LocationBean[][] locationArray) {
		int rows = locationArray[0].length;
		int cols = locationArray.length;
		LocationBean[][] groupedLocationArray = new LocationBean[cols][rows];
		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				for (LocationBean location : grouping) {
					if (location == locationArray[x][y]) {
						groupedLocationArray[x][y] = new LocationBean(1.0,1.0, location.getWeight());
					}
				}
			}
		}
		
		printLocationWeightsAsMap(groupedLocationArray);
	}
	
}
