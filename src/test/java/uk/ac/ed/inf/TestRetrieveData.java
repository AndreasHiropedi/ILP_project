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
 * Unit test for the RetrieveData class
 * (tests that the retrieval of information from the REST server
 * works as expected)
 */
public class TestRetrieveData
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public TestRetrieveData() throws MalformedURLException
    {

    }

    // =========================================================================
    // ================================ TESTS ==================================
    // =========================================================================

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // the base URL address (no extensions) for the REST server
    URL baseUrl = new URL("https://ilp-rest.azurewebsites.net");

    @Test
    @DisplayName("Testing if all orders are retrieved correctly")
    void testTotalOrdersRetrieval()
    {
        List<Order> allOrders = RetrieveData.getData(baseUrl, "/orders", new TypeReference<>(){});
        assertEquals(7050, allOrders.size());
    }

    @Test
    @DisplayName("Testing if all orders for a given date are retrieved correctly")
    void testOrdersForGivenDateRetrieval()
    {
        String date = "2023-04-15";
        String extension = "/orders" + "/" + date;
        List<Order> allOrdersForGivenDate = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        assertEquals(47, allOrdersForGivenDate.size());
    }

    @Test
    @DisplayName("Testing if the central campus area is retrieved correctly")
    void testCentralAreaRetrieval()
    {
        List<LngLat> centralAreaVertices = RetrieveData.getData(baseUrl, "/centralArea", new TypeReference<>(){});
        assertEquals(4, centralAreaVertices.size());
    }

    @Test
    @DisplayName("Testing if all restaurants are retrieved correctly")
    void testRestaurantRetrieval()
    {
        List<Restaurant> allRestaurants = RetrieveData.getData(baseUrl, "/restaurants", new TypeReference<>(){});
        assertEquals(4, allRestaurants.size());
    }

    @Test
    @DisplayName("Testing if all no-fly-zones are retrieved correctly")
    void testNoFlyZoneRetrieval()
    {
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, "/noFlyZones", new TypeReference<>(){});
        assertEquals(4, allNoFlyZones.size());
    }

}
