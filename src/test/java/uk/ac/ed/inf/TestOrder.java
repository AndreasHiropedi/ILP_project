package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    URL base = new URL("https://ilp-rest.azurewebsites.net");
    Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(base);
    ArrayList<Order> orders = RetrieveData.getData(base, "/orders", new TypeReference<>(){});
    Order order1 = orders.get(0);
    Order order2 = orders.get(2);
    Order order3 = orders.get(11);
    Menu item = new Menu("Joe's special pizza", 55000);
    List<Menu> menuTests = Arrays.stream(new Menu[]{item}).toList();
    Restaurant restaurants1 = new Restaurant("Johnny's", menuTests, -3.82, 55.67);

    @Test
    @DisplayName("Testing different scenarios for getDeliveryCost method")
    public void testGetDeliveryCost() throws InvalidPizzaCombinationException 
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
        // test also for the exception
        String finalItem = item1;
        String finalItem1 = item2;
        InvalidPizzaCombinationException exception = assertThrows(InvalidPizzaCombinationException.class,
                () -> order1.getDeliveryCost(new Restaurant[]{restaurants1}, finalItem, finalItem1),
                "Didn't throw the exception");
        assertTrue(exception.getMessage().contains("This pizza combination is invalid!"));
        System.out.println("GetDeliveryCost method works as expected!");
    }

}
