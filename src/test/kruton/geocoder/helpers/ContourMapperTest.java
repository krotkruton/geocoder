package test.kruton.geocoder.helpers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.kruton.geocoder.beans.LocationBean;
import com.kruton.geocoder.helpers.ContourHelper;
import com.kruton.geocoder.utils.Debug;
import com.kruton.geocoder.utils.Debug.LEVEL;

public class ContourMapperTest {
	LEVEL debug_level = Debug.LEVEL.NONE;
	
	private String hospitalsFile = "C:\\src\\Geocoder\\resources\\hospitals.csv";
	ArrayList<LocationBean> testLocations1 = new ArrayList<LocationBean>();
	ArrayList<LocationBean> testLocations2 = new ArrayList<LocationBean>();
	ArrayList<LocationBean> testLocations3 = new ArrayList<LocationBean>();
	
	@Before
	public void setUp() throws Exception {
		testLocations1.add(new LocationBean(10.0, 10.0, 10.0));
		testLocations1.add(new LocationBean(10.0, 11.0, 11.1));
		testLocations1.add(new LocationBean(10.0, 12.0, 12.2));
		testLocations1.add(new LocationBean(13.0, 13.0, 13.3));
		testLocations1.add(new LocationBean(11.0, 13.0, 14.4));
		testLocations1.add(new LocationBean(10.0, 13.0, 15.5));
		testLocations1.add(new LocationBean(11.0, 10.0, 16.6));
		testLocations1.add(new LocationBean(11.0, 11.0, 17.7));
		testLocations1.add(new LocationBean(11.0, 12.0, 18.8));
		testLocations1.add(new LocationBean(12.0, 12.0, 19.9));
		testLocations1.add(new LocationBean(12.0, 13.0, 20.0));
		testLocations1.add(new LocationBean(13.0, 10.0, 21.1));
		testLocations1.add(new LocationBean(13.0, 11.0, 22.2));
		testLocations1.add(new LocationBean(13.0, 12.0, 23.3));
		testLocations1.add(new LocationBean(12.0, 10.0, 24.4));
		testLocations1.add(new LocationBean(12.0, 11.0, 25.5));
		
		testLocations2.add(new LocationBean(10.0, 10.0, 10.0));
		testLocations2.add(new LocationBean(10.0, 12.0, 11.1));
		testLocations2.add(new LocationBean(11.0, 10.0, 12.2));
		testLocations2.add(new LocationBean(11.0, 12.0, 13.3));
		
		
		testLocations3.add(new LocationBean(10.0, 10.0, 10.0));
		testLocations3.add(new LocationBean(10.0, 11.0, 18.1));
		testLocations3.add(new LocationBean(10.0, 12.0, 15.2));
		testLocations3.add(new LocationBean(13.0, 13.0, 13.3));
		testLocations3.add(new LocationBean(11.0, 13.0, 22.4));
		testLocations3.add(new LocationBean(10.0, 13.0, 13.5));
		testLocations3.add(new LocationBean(11.0, 10.0, 16.6));
		testLocations3.add(new LocationBean(11.0, 11.0, 17.7));
		testLocations3.add(new LocationBean(11.0, 12.0, 18.8));
		testLocations3.add(new LocationBean(12.0, 12.0, 19.9));
		testLocations3.add(new LocationBean(12.0, 13.0, 20.0));
		testLocations3.add(new LocationBean(13.0, 10.0, 21.1));
		testLocations3.add(new LocationBean(13.0, 11.0, 22.2));
		testLocations3.add(new LocationBean(13.0, 12.0, 18.3));
		testLocations3.add(new LocationBean(12.0, 10.0, 24.4));
		testLocations3.add(new LocationBean(12.0, 11.0, 18.5));
	}
	
	@Test
	public void LocationSortTest() {
		System.out.println("************ LocationSortTest");
		ContourHelper counterHelper = new ContourHelper(testLocations1);
		counterHelper.sortLocations();
		
		for(LocationBean location : counterHelper.getLocations())
			System.out.println(location.getLatitude() + ", " + location.getLongitude());
		
		System.out.println();
		
		
		assertEquals(testLocations1.get(0).getWeight(), (Double) 10.0);
		assertEquals(testLocations1.get(15).getWeight(), (Double) 13.3);
		
		
		
		System.out.println(testLocations1.get(5).getWeight() + " " + Math.round(testLocations1.get(5).getWeight()));
		System.out.println(testLocations1.get(15).getWeight() + " " + Math.round(testLocations1.get(15).getWeight()));
	}
	
	@Test
	public void LocationArrayConversionTest() {
		System.out.println("************ LocationArrayConversionTest");
		ContourHelper counterHelper = new ContourHelper(testLocations1);
		counterHelper.sortLocations();

		int rows = 4;
		int cols = 4;
		LocationBean[][] locationArray = counterHelper.createLocationArrayFromList(rows, cols);
		
		System.out.println(locationArray[0].length + " " + locationArray.length);
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		System.out.println();
		System.out.flush();
		
		
	}
	
	
	//@Test
	public void LocationArrayExpansionTest() {
		System.out.println("************ LocationArrayExpansionTest");
		ContourHelper counterHelper = new ContourHelper(testLocations1);
		counterHelper.sortLocations();

		int rows = 4;
		int cols = 4;
		LocationBean[][] locationArray = counterHelper.createLocationArrayFromList(rows, cols);
		
		System.out.println("array width and height: " + locationArray[0].length + " " + locationArray.length);
		
		locationArray = counterHelper.expandArray(locationArray);
		
		System.out.println("array width and height: " + locationArray[0].length + " " + locationArray.length);
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);

		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		
		//counterHelper.printLocationWeightsAsMap(locationArray);
		
		counterHelper.printLocationsWithWeights(locationArray);
	}
	
	//@Test
	public void SettingLatsAndLonsTest() {
		System.out.println("************ SettingLatsAndLonsTest");
		ContourHelper counterHelper = new ContourHelper(testLocations2);
		counterHelper.sortLocations();

		int rows = 2;
		int cols = 2;
		LocationBean[][] locationArray = counterHelper.createLocationArrayFromList(rows, cols);
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		System.out.println(locationArray[0][0].getLatitude() + " " + locationArray[0][0].getLongitude());
		System.out.println(locationArray[0][1].getLatitude() + " " + locationArray[0][1].getLongitude());
		System.out.println(locationArray[1][0].getLatitude() + " " + locationArray[1][0].getLongitude());
		System.out.println(locationArray[1][1].getLatitude() + " " + locationArray[1][1].getLongitude());
		
		counterHelper.printLocationsWithWeights(locationArray);
		//counterHelper.printLocationWeightsAsMap(locationArray);
		counterHelper.printLatLonsWeightsAsMap(locationArray);
		
	}

	@Test
	public void CreateTestDataForPostGres() {
		System.out.println("************ CreateTestDataForPostGres");
		ContourHelper counterHelper = new ContourHelper(testLocations3);
		counterHelper.sortLocations();

		int rows = 4;
		int cols = 4;
		LocationBean[][] locationArray = counterHelper.createLocationArrayFromList(rows, cols);
		
		System.out.println("array width and height: " + locationArray[0].length + " " + locationArray.length);
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);

		
		//counterHelper.printLocationWeightsAsMap(locationArray);
		
		counterHelper.printLocationsWithWeights(locationArray);
	}
	
	//@Test
	public void WeightedGroupingText() {
		System.out.println("************ WeightedGroupingText");
		ContourHelper counterHelper = new ContourHelper(testLocations1);
		counterHelper.sortLocations();

		int rows = 4;
		int cols = 4;
		LocationBean[][] locationArray = counterHelper.createLocationArrayFromList(rows, cols);
		
		locationArray = counterHelper.expandArray(locationArray);
		
		counterHelper.fillInArray(locationArray, debug_level);
		
		counterHelper.groupLocationsByWeight(locationArray);
		
		Map<Integer, List<LocationBean>> groupings = counterHelper.groupLocationsByWeight(locationArray);
		
		System.out.println(groupings.size());
		
		counterHelper.printLocationWeightsAsMap(locationArray);
		
		List<LocationBean> grouping = groupings.get(14);
		counterHelper.printGrouping(grouping, locationArray);
		
		assertEquals(grouping.size(), 4);
	
		grouping = groupings.get(15);
		
		assertEquals(grouping.size(), 4);
	}
}
