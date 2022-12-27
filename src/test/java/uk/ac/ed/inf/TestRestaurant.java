package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the Restaurant class
 * (ensures all individual public methods in the class work as expected)
 */
public class TestRestaurant
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public TestRestaurant() throws MalformedURLException
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
    @DisplayName("Testing if all restaurants on the REST server are retrieved correctly")
    void testGetRestaurantsFromServer()
    {
        Restaurant[] allRestaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        assertEquals(4, allRestaurants.length);
    }

    @Test
    @DisplayName("Testing if the menu items array is generated correctly")
    void testMenuItemsAsArrayInstantiation()
    {
        Restaurant[] allRestaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Menu[] menuItems = allRestaurants[0].getMenu();
        List<Menu> menuItemsAsList = allRestaurants[0].getMenuItems();
        assertEquals(menuItemsAsList.size(), menuItems.length);
    }

}
