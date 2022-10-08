package uk.ac.ed.inf;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRestaurant
{

    public TestRestaurant() throws MalformedURLException
    {

    }

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    URL base = new URL("https://ilp-rest.azurewebsites.net");
    Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(base);
    List<Restaurant> restaurantsList = Arrays.stream(restaurants).toList();
    Restaurant restaurant1 = restaurantsList.get(2);

    @Test
    @DisplayName("Testing different scenarios for getMenu method")
    public void testGetMenu()
    {
        Menu[] menuItems = restaurant1.getMenu();
        assertEquals(restaurant1.menuItems().size(), Arrays.stream(menuItems).toList().size());
        assertEquals(restaurant1.menuItems().get(1).name(), Arrays.stream(menuItems).toList().get(1).name());
        assertEquals(restaurant1.menuItems().get(1).priceInPence(), Arrays.stream(menuItems).toList().get(1).priceInPence());
        assertEquals(restaurant1.menuItems(), Arrays.stream(menuItems).toList());
        System.out.println("GetMenu method works as expected!");
    }

}
