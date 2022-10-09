package uk.ac.ed.inf;

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

    ArrayList<Order> orders = RetrieveData.getInstance().retrieveOrderData();
    URL base = new URL("https://ilp-rest.azurewebsites.net");
    Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(base);
    Order order1 = orders.get(0);
    Order order2 = orders.get(2);
    Order order3 = orders.get(11);

    @Test
    @DisplayName("Testing different scenarios for getDeliveryCost method")
    public void testGetDeliveryCost()
    {
        String item1 = order1.orderItems().get(0);
        String item2 = order1.orderItems().get(1);
        assertEquals(2600, order1.getDeliveryCost(restaurants, item1, item2));
        item1 = order2.orderItems().get(0);
        item2 = order2.orderItems().get(1);
        assertEquals(2400, order2.getDeliveryCost(restaurants, item1, item2));
        item1 = order3.orderItems().get(0);
        item2 = order3.orderItems().get(1);
        assertEquals(2500, order3.getDeliveryCost(restaurants, item1, item2));
        System.out.println("GetDeliveryCost method works as expected!");
    }

}
