package uk.ac.ed.inf;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestLngLat
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    LngLat point1 = new LngLat(-3.192473, 55.946233);
    LngLat point2 = new LngLat(-3.192473, 55.942617);
    LngLat point3 = new LngLat(-3.184319, 55.942617);
    LngLat point4 = new LngLat(-3.184319, 55.946233);
    LngLat point5 = new LngLat(-3.184319, 55.943620);
    LngLat point6 = new LngLat(-3.184330, 55.946233);
    LngLat point7 = new LngLat(-3.184319, 60);
    LngLat point8 = new LngLat(-4,55.946233);
    LngLat point9 = new LngLat(-3.184330, 55.942620);
    LngLat point10 = new LngLat(-4, -60);
    LngLat point11 = new LngLat(-3.192623,55.946233);

    @Test
    @DisplayName("Testing different scenarios for inCentralArea method")
    public void testInCentralArea() throws MalformedURLException
    {
        assertTrue(point1.inCentralArea());
        assertTrue(point2.inCentralArea());
        assertTrue(point3.inCentralArea());
        assertTrue(point4.inCentralArea());
        assertTrue(point5.inCentralArea());
        assertTrue(point6.inCentralArea());
        assertTrue(point9.inCentralArea());
        assertFalse(point7.inCentralArea());
        assertFalse(point8.inCentralArea());
        assertFalse(point10.inCentralArea());
        assertFalse(point11.inCentralArea());
        System.out.println("Central area method works as expected!");
    }

    @Test
    @DisplayName("Testing different scenarios for distanceTo method")
    public void testDistanceTo()
    {
        assertEquals(115.946233, point8.distanceTo(point10));
        assertEquals(0.003616000000000952, point1.distanceTo(point2));
        assertEquals(0, point1.distanceTo(point1));
        System.out.println("DistanceTo method works as expected!");
    }

    @Test
    @DisplayName("Testing different scenarios for closeTo method")
    public void testCloseTo()
    {
        assertTrue(point1.closeTo(point1));
        assertFalse(point1.closeTo(point2));
        assertFalse(point1.closeTo(point11));
        System.out.println("CloseTo method works as expected!");
    }

    @Test
    @DisplayName("Testing different scenarios for nextPosition method")
    public void testNextPosition()
    {
        assertEquals(point1, point1.nextPosition(null));
        assertEquals(point1.lng() + 0.00015, point1.nextPosition(CompassLocation.EAST).lng());
        assertEquals(point1.lat(), point1.nextPosition(CompassLocation.EAST).lat());
        assertEquals(point1.lat()+ 0.00015, point1.nextPosition(CompassLocation.NORTH).lat());
        assertEquals(point1.lng(), point1.nextPosition(CompassLocation.NORTH).lng());
        System.out.println("Next position method works as expected!");
    }

}
