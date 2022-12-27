package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the LngLat class
 * (ensures all individual public methods in the class work as expected)
 */
public class TestLngLat
{

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // 5 points, covering each case for the central area (in this order):
    // vertex, on vertical edge, on horizontal edge, fully inside, fully outside
    LngLat point1 = new LngLat(-3.192473, 55.946233);
    LngLat point2 = new LngLat(-3.192473, 55.943);
    LngLat point3 = new LngLat(-3.19, 55.946233);
    LngLat point4 = new LngLat(-3.19, 55.943);
    LngLat point5 = new LngLat(-4, 60);

    @Test
    @DisplayName("Testing if the inCentralArea() method works as expected")
    void testInCentralArea() throws MalformedURLException
    {
        LngLat.setBaseUrl(new URL("https://ilp-rest.azurewebsites.net"));
        assertTrue(point1.inCentralArea());
        assertTrue(point2.inCentralArea());
        assertTrue(point3.inCentralArea());
        assertTrue(point4.inCentralArea());
        assertFalse(point5.inCentralArea());
    }

    // points covering each case for each of the no-fly-zones (in this order):
    // vertex, on edge, fully inside, fully outside

    // George Square area
    LngLat nf1point1 = new LngLat(-3.190578818321228, 55.94402412577528);
    LngLat nf1point2 = new LngLat(-3.1899887323379517, 55.94284650540911);
    LngLat nf1point3 = new LngLat(-3.19, 55.944);
    LngLat nf1point4 = new LngLat(-3.2, 56);

    // Dr Elsie Inglis Quadrangle
    LngLat nf2point1 = new LngLat(-3.1907182931900024, 55.94519570234043);
    LngLat nf2point2 = new LngLat(-3.1906163692474365, 55.94498241796357);
    LngLat nf2point3 = new LngLat(-3.1903, 55.9451);
    LngLat nf2point4 = new LngLat(-3.2, 57);

    // Bristo Square Open Area
    LngLat nf3point1 = new LngLat(-3.189543485641479, 55.94552313663306);
    LngLat nf3point2 = new LngLat(-3.189382553100586, 55.94553214854692);
    LngLat nf3point3 = new LngLat(-3.188551068305969, 55.94610590274561);
    LngLat nf3point4 = new LngLat(-3.19, 56);

    // Bayes Central Area
    LngLat nf4point1 = new LngLat(-3.1876927614212036, 55.94520696732767);
    LngLat nf4point2 = new LngLat(-3.187555968761444, 55.9449621408666);
    LngLat nf4point3 = new LngLat(-3.1875, 55.9451);
    LngLat nf4point4 = new LngLat(-3.19, 57);

    @Test
    @DisplayName("Testing if the inNoFlyZone() method works as expected")
    void testInNoFlyZone() throws MalformedURLException
    {
        URL baseURL = new URL("https://ilp-rest.azurewebsites.net");
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseURL, "/noFlyZones", new TypeReference<>(){});
        // check points for no-fly-zone 1
        assertTrue(nf1point1.inNoFlyZone());
        assertTrue(nf1point2.inNoFlyZone());
        assertTrue(nf1point3.inNoFlyZone());
        assertFalse(nf1point4.inNoFlyZone());
        // check points for no-fly-zone 2
        assertTrue(nf2point1.inNoFlyZone());
        assertTrue(nf2point2.inNoFlyZone());
        assertTrue(nf2point3.inNoFlyZone());
        assertFalse(nf2point4.inNoFlyZone());
        // check points for no-fly-zone 3
        assertTrue(nf3point1.inNoFlyZone());
        assertTrue(nf3point2.inNoFlyZone());
        assertTrue(nf3point3.inNoFlyZone());
        assertFalse(nf3point4.inNoFlyZone());
        // check points for no-fly-zone 4
        assertTrue(nf4point1.inNoFlyZone());
        assertTrue(nf4point2.inNoFlyZone());
        assertTrue(nf4point3.inNoFlyZone());
        assertFalse(nf4point4.inNoFlyZone());
    }

    // edge case for the closeTo() method (distance to point1 is exactly 0.00015)
    LngLat point6 = new LngLat(-3.1924, 55.94637);

    @Test
    @DisplayName("Testing if the closeTo() method works as expected")
    void testCloseTo()
    {
        assertTrue(point1.closeTo(point1));
        assertFalse(point1.closeTo(point6));
        assertFalse(point1.closeTo(point5));
    }

    @Test
    @DisplayName("Testing if the nextPosition() method works as expected")
    void testNextPosition()
    {
        assertEquals(point1, point1.nextPosition(null));
        assertEquals(-3.192323, (point2.nextPosition(CompassDirection.EAST)).getLng());
        assertEquals(55.943, point2.nextPosition(CompassDirection.EAST).getLat());
        assertEquals(-3.192457402514855, (point6.nextPosition(CompassDirection.NORTH_NORTH_WEST)).getLng());
        assertEquals(55.946508581929876, (point6.nextPosition(CompassDirection.NORTH_NORTH_WEST)).getLat());
        assertEquals(-3.189893933982822, (point4.nextPosition(CompassDirection.SOUTH_EAST)).getLng());
        assertEquals(55.94289393398282, (point4.nextPosition(CompassDirection.SOUTH_EAST)).getLat());
        assertEquals(-3.1901385819298764, (point3.nextPosition(CompassDirection.WEST_NORTH_WEST)).getLng());
        assertEquals(55.946290402514855, (point3.nextPosition(CompassDirection.WEST_NORTH_WEST)).getLat());
    }

    @Test
    @DisplayName("Testing if the distanceTo() method works as expected")
    void testDistanceTo()
    {
        assertEquals(0, point1.distanceTo(point1));
        assertEquals(0.0040703830286609585, point1.distanceTo(point4));
        assertEquals(4.136586618908036, point2.distanceTo(point5));
    }

    // edge case for the line intersections
    // (both points outside no-fly-zone but line still goes through no-fly-zone)
    LngLat point7 = new LngLat(-3.186874, 55.944494);

    @Test
    @DisplayName("Testing if the lineCutsThroughNoFlyZones() method works as expected")
    void testLineCutsThroughNoFlyZones() throws MalformedURLException
    {
        URL baseURL = new URL("https://ilp-rest.azurewebsites.net");
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseURL, "/noFlyZones", new TypeReference<>(){});
        // check line intersections
        assertFalse(point1.lineCutsThroughNoFlyZones(point5));
        assertTrue(point3.lineCutsThroughNoFlyZones(nf2point3));
        assertTrue(nf2point1.lineCutsThroughNoFlyZones(nf2point3));
        assertTrue(point1.lineCutsThroughNoFlyZones(nf1point1));
        assertTrue(point1.lineCutsThroughNoFlyZones(point7));
    }

}
