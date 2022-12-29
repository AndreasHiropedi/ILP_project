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
 * Integration testing for the Order class
 * (tests orders retrieved from the REST server instead of mock ones)
 */
public class OrderIntegrationTest
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public OrderIntegrationTest() throws MalformedURLException
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
    @DisplayName("Testing if order validation for REST server data for all orders works as expected")
    void testOrderValidationForAllOrders()
    {
        // set up data before checking order validation
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(baseUrl));
        List<Order> allOrders = RetrieveData.getData(baseUrl, "/orders", new TypeReference<>(){});
        // order validation data
        int invalidTotal = 0;
        int invalidCvv = 0;
        int invalidExpiryDate = 0;
        int invalidCardNumber = 0;
        int invalidPizzaCount = 0;
        int invalidNotFound = 0;
        int invalidMultipleSuppliers = 0;
        int valid = 0;
        for (Order o: allOrders)
        {
            switch (o.getOutcome())
            {
                case InvalidCardNumber -> invalidCardNumber += 1;
                case InvalidTotal -> invalidTotal += 1;
                case InvalidCvv -> invalidCvv += 1;
                case InvalidExpiryDate -> invalidExpiryDate += 1;
                case InvalidPizzaCount -> invalidPizzaCount += 1;
                case InvalidPizzaCombinationMultipleSuppliers -> invalidMultipleSuppliers += 1;
                case InvalidPizzaNotDefined -> invalidNotFound += 1;
                default -> valid += 1;
            }
        }
        // checks being performed
        assertEquals(150, invalidCardNumber);
        assertEquals(150, invalidExpiryDate);
        assertEquals(150, invalidCvv);
        assertEquals(150, invalidTotal);
        assertEquals(150, invalidPizzaCount);
        assertEquals(150, invalidNotFound);
        assertEquals(150, invalidMultipleSuppliers);
        assertEquals(6000, valid);
    }

    @Test
    @DisplayName("Testing if order validation for REST server data for orders on a given date works as expected (1)")
    void testOrderValidationForOrdersOnGivenDate1()
    {
        // set up data before checking order validation
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(baseUrl));
        List<Order> allOrders = RetrieveData.getData(baseUrl, "/orders/2023-01-15", new TypeReference<>(){});
        // order validation data
        int invalidTotal = 0;
        int invalidCvv = 0;
        int invalidExpiryDate = 0;
        int invalidCardNumber = 0;
        int invalidPizzaCount = 0;
        int invalidNotFound = 0;
        int invalidMultipleSuppliers = 0;
        int valid = 0;
        for (Order o: allOrders)
        {
            switch (o.getOutcome())
            {
                case InvalidCardNumber -> invalidCardNumber += 1;
                case InvalidTotal -> invalidTotal += 1;
                case InvalidCvv -> invalidCvv += 1;
                case InvalidExpiryDate -> invalidExpiryDate += 1;
                case InvalidPizzaCount -> invalidPizzaCount += 1;
                case InvalidPizzaCombinationMultipleSuppliers -> invalidMultipleSuppliers += 1;
                case InvalidPizzaNotDefined -> invalidNotFound += 1;
                default -> valid += 1;
            }
        }
        // checks being performed
        assertEquals(1, invalidCardNumber);
        assertEquals(1, invalidExpiryDate);
        assertEquals(1, invalidCvv);
        assertEquals(1, invalidTotal);
        assertEquals(1, invalidPizzaCount);
        assertEquals(1, invalidNotFound);
        assertEquals(1, invalidMultipleSuppliers);
        assertEquals(40, valid);
    }

    @Test
    @DisplayName("Testing if order validation for REST server data for orders on a given date works as expected (2)")
    void testOrderValidationForOrdersOnGivenDate2()
    {
        // set up data before checking order validation
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(baseUrl));
        List<Order> allOrders = RetrieveData.getData(baseUrl, "/orders/2023-02-02", new TypeReference<>(){});
        // order validation data
        int invalidTotal = 0;
        int invalidCvv = 0;
        int invalidExpiryDate = 0;
        int invalidCardNumber = 0;
        int invalidPizzaCount = 0;
        int invalidNotFound = 0;
        int invalidMultipleSuppliers = 0;
        int valid = 0;
        for (Order o: allOrders)
        {
            switch (o.getOutcome())
            {
                case InvalidCardNumber -> invalidCardNumber += 1;
                case InvalidTotal -> invalidTotal += 1;
                case InvalidCvv -> invalidCvv += 1;
                case InvalidExpiryDate -> invalidExpiryDate += 1;
                case InvalidPizzaCount -> invalidPizzaCount += 1;
                case InvalidPizzaCombinationMultipleSuppliers -> invalidMultipleSuppliers += 1;
                case InvalidPizzaNotDefined -> invalidNotFound += 1;
                default -> valid += 1;
            }
        }
        // checks being performed
        assertEquals(1, invalidCardNumber);
        assertEquals(1, invalidExpiryDate);
        assertEquals(1, invalidCvv);
        assertEquals(1, invalidTotal);
        assertEquals(1, invalidPizzaCount);
        assertEquals(1, invalidNotFound);
        assertEquals(1, invalidMultipleSuppliers);
        assertEquals(40, valid);
    }

}
