package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the Order class
 * (ensures all individual public methods in the class work as expected)
 */
public class TestOrder
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public TestOrder() throws MalformedURLException
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
    @DisplayName("Testing if order validation works as expected")
    void testOrderValidation()
    {
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(baseUrl));
        // list of mock order objects (both valid and invalid)
        Order order1 = new Order("1AFFE082","2023-01-01",
                "Gilberto Handshoe","2402902","04/28","922",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms")));
        Order order2 = new Order("67D62E36","2023-01-01",
                "Mason Sporman","2720038402742337","05/12","819",
                2400, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese")));
        Order order3 = new Order("2240F9BC","2023-01-01",
                "Alana Okel","5468311886275606","09/28","2715",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        Order order4 = new Order("3EDE207C","2023-01-01",
                "Janelle Rohla","4478588268902474","02/25","108",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms","Pizza-Surprise -498702880")));
        Order order5 = new Order("6526E742","2023-01-01",
                "Mira Starzyk","4132245423947435","09/26","668",
                2712, new ArrayList<>(Arrays.asList("Meat Lover","Vegan Delight")));
        Order order6 = new Order("0FC19839", "2023-01-01",
                "Silas Shiers","2720159070630230","10/26","829",
                11600, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese",
                "Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese",
                "Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese")));
        Order order7 = new Order("2AEB25EC", "2023-01-01",
                "Chin Oharra","4306685979511715","09/24","666",
                3900,new ArrayList<>(Arrays.asList("Margarita","Calzone","Meat Lover")));
        Order validOrder1 = new Order("7157DF75", "2023-01-01",
                "Harlan Kimery","5480088966844071","06/28","641",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        Order validOrder2 = new Order("5002C4EA", "2023-01-01",
                "Sammie Irey","5208550824338597","10/27","771",
                2600, new ArrayList<>(Arrays.asList("Meat Lover","Vegan Delight")));
        Order validOrder3 = new Order("72156288", "2023-01-01",
                "Richie Eadie","4206755744907018","08/24","544",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms")));
        Order validOrder4 = new Order("46D871D4", "2023-01-01",
                "Indira Petrillo","5312297448366363","10/26","974",
                2400, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese")));
        Order validOrder5 = new Order("63FD3ECE", "2023-01-01",
                "Andra Guidotti","5552158567261299","01/27","260",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        // checks being performed
        assertEquals(5, Order.getValidOrders().size());
        assertEquals(OrderOutcome.InvalidCardNumber, order1.getOutcome());
        assertEquals(OrderOutcome.InvalidExpiryDate, order2.getOutcome());
        assertEquals(OrderOutcome.InvalidCvv, order3.getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaNotDefined, order4.getOutcome());
        assertEquals(OrderOutcome.InvalidTotal, order5.getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaCount, order6.getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers, order7.getOutcome());
        assertEquals(OrderOutcome.ValidButNotDelivered,validOrder1.getOutcome());
        assertEquals(OrderOutcome.ValidButNotDelivered, validOrder4.getOutcome());
    }


    @Test
    @DisplayName("Testing if the updateDeliveryCost() method works as expected")
    void testUpdateDeliveryCost()
    {
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(baseUrl));
        // list of mock order objects (both valid and invalid)
        Order order1 = new Order("1AFFE082","2023-01-01",
                "Gilberto Handshoe","2402902","04/28","922",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms")));
        Order order2 = new Order("67D62E36","2023-01-01",
                "Mason Sporman","2720038402742337","05/12","819",
                2400, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese")));
        Order order3 = new Order("2240F9BC","2023-01-01",
                "Alana Okel","5468311886275606","09/28","2715",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        Order order4 = new Order("3EDE207C","2023-01-01",
                "Janelle Rohla","4478588268902474","02/25","108",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms","Pizza-Surprise -498702880")));
        Order order5 = new Order("6526E742","2023-01-01",
                "Mira Starzyk","4132245423947435","09/26","668",
                2712, new ArrayList<>(Arrays.asList("Meat Lover","Vegan Delight")));
        Order order6 = new Order("0FC19839", "2023-01-01",
                "Silas Shiers","2720159070630230","10/26","829",
                11600, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese",
                "Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese",
                "Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese")));
        Order order7 = new Order("2AEB25EC", "2023-01-01",
                "Chin Oharra","4306685979511715","09/24","666",
                3900,new ArrayList<>(Arrays.asList("Margarita","Calzone","Meat Lover")));
        Order validOrder1 = new Order("7157DF75", "2023-01-01",
                "Harlan Kimery","5480088966844071","06/28","641",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        Order validOrder2 = new Order("5002C4EA", "2023-01-01",
                "Sammie Irey","5208550824338597","10/27","771",
                2600, new ArrayList<>(Arrays.asList("Meat Lover","Vegan Delight")));
        Order validOrder3 = new Order("72156288", "2023-01-01",
                "Richie Eadie","4206755744907018","08/24","544",
                2400, new ArrayList<>(Arrays.asList("Super Cheese","All Shrooms")));
        Order validOrder4 = new Order("46D871D4", "2023-01-01",
                "Indira Petrillo","5312297448366363","10/26","974",
                2400, new ArrayList<>(Arrays.asList("Proper Pizza","Pineapple & Ham & Cheese")));
        Order validOrder5 = new Order("63FD3ECE", "2023-01-01",
                "Andra Guidotti","5552158567261299","01/27","260",
                2500, new ArrayList<>(Arrays.asList("Margarita","Calzone")));
        // checks being performed
        order4.updateDeliveryCost();
        order5.updateDeliveryCost();
        validOrder1.updateDeliveryCost();
        validOrder2.updateDeliveryCost();
        validOrder3.updateDeliveryCost();
        validOrder4.updateDeliveryCost();
        validOrder5.updateDeliveryCost();
        assertEquals(2500, validOrder1.getOrderDeliveryCost());
        assertEquals(2600, validOrder2.getOrderDeliveryCost());
        assertEquals(2400, validOrder3.getOrderDeliveryCost());
        assertEquals(2400, validOrder4.getOrderDeliveryCost());
        assertEquals(2500, validOrder5.getOrderDeliveryCost());
        assertEquals(0, order4.getOrderDeliveryCost());
        assertEquals(2600, order5.getOrderDeliveryCost());
    }

}
