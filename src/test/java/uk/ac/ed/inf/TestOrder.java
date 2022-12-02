package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrder
{

    public TestOrder() throws MalformedURLException
    {

    }

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }



    @Test
    @DisplayName("Testing different scenarios for getDeliveryCost method")
    public void testGetDeliveryCost() throws MalformedURLException {
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net"));
        Order.setRestaurants(restaurants);
        ArrayList<Order> orders = RetrieveData.getData(new URL("https://ilp-rest.azurewebsites.net"), "/orders", new TypeReference<>(){});
        Order order1 = orders.get(0);
        Order order2 = orders.get(2);
        Order order3 = orders.get(11);
        assertEquals(2400, order1.getDeliveryCost());
        assertEquals(2500, order2.getDeliveryCost());
        assertEquals(100, order3.getDeliveryCost());
        System.out.println("GetDeliveryCost method works as expected!");
    }

}
