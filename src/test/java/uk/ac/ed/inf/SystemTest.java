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
 * System test for the whole PizzaDronz application
 * (test the drone based on certain dates and information from the REST server)
 */
public class SystemTest
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public SystemTest() throws MalformedURLException
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

    // base URL for REST server
    URL baseUrl = new URL("https://ilp-rest.azurewebsites.net");

    @Test
    @DisplayName("Testing if the whole PizzaDronz app works as expected (1)")
    void testWholeSystem1()
    {
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders/2023-01-11";
        List<Order> allOrders = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(baseUrl);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan("2023-01-11");
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);

        // check to see if order outcome has changed for delivered orders
        int totalDelivered = 0;
        for (Order order: Order.getValidOrders())
        {
            if (order.getOutcome().equals(OrderOutcome.Delivered))
            {
                totalDelivered += 1;
            }
        }
        assertEquals(20, totalDelivered);

        // TODO: implement checks to ensure appropriate files are created

    }

    @Test
    @DisplayName("Testing if the whole PizzaDronz app works as expected (2)")
    void testWholeSystem2()
    {
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders/2023-02-03";
        List<Order> allOrders = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(baseUrl);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan("2023-02-03");
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);

        // check to see if order outcome has changed for delivered orders
        int totalDelivered = 0;
        for (Order order: Order.getValidOrders())
        {
            if (order.getOutcome().equals(OrderOutcome.Delivered))
            {
                totalDelivered += 1;
            }
        }
        assertEquals(20, totalDelivered);

        // TODO: implement checks to ensure appropriate files are created

    }

    @Test
    @DisplayName("Testing if the whole PizzaDronz app works as expected (3)")
    void testWholeSystem3()
    {
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders/2023-03-21";
        List<Order> allOrders = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(baseUrl);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan("2023-03-21");
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);

        // check to see if order outcome has changed for delivered orders
        int totalDelivered = 0;
        for (Order order: Order.getValidOrders())
        {
            if (order.getOutcome().equals(OrderOutcome.Delivered))
            {
                totalDelivered += 1;
            }
        }
        assertEquals(20, totalDelivered);

        // TODO: implement checks to ensure appropriate files are created

    }

    @Test
    @DisplayName("Testing if the whole PizzaDronz app works as expected (4)")
    void testWholeSystem4()
    {
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders/2023-04-13";
        List<Order> allOrders = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(baseUrl);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan("2023-04-13");
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);

        // check to see if order outcome has changed for delivered orders
        int totalDelivered = 0;
        for (Order order: Order.getValidOrders())
        {
            if (order.getOutcome().equals(OrderOutcome.Delivered))
            {
                totalDelivered += 1;
            }
        }
        assertEquals(20, totalDelivered);

        // TODO: implement checks to ensure appropriate files are created

    }

    @Test
    @DisplayName("Testing if the whole PizzaDronz app works as expected (5)")
    void testWholeSystem5()
    {
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders/2023-05-22";
        List<Order> allOrders = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(baseUrl, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(baseUrl);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan("2023-05-22");
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);

        // check to see if order outcome has changed for delivered orders
        int totalDelivered = 0;
        for (Order order: Order.getValidOrders())
        {
            if (order.getOutcome().equals(OrderOutcome.Delivered))
            {
                totalDelivered += 1;
            }
        }
        assertEquals(20, totalDelivered);

        // TODO: implement checks to ensure appropriate files are created

    }

}
