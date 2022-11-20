package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.InvalidPizzaCombinationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * this class is used to store all the information about orders
 * from the REST server
 */
public class Order
{
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("orderDate")
    private String orderDate;
    @JsonProperty("customer")
    private String customer;
    @JsonProperty("creditCardNumber")
    private String creditCardNumber;
    @JsonProperty("creditCardExpiry")
    private String creditCardExpiry;
    @JsonProperty("cvv")
    private String cvv;
    @JsonProperty("priceTotalInPence")
    private int priceTotalInPence;
    @JsonProperty("orderItems")
    private List<String> orderItems;

    // this field is used to filter out invalid orders
    private OrderOutcome outcome;

    /**
     * constructor for the order objects
     * @param orderNo: the unique number used to identify an order
     * @param orderDate: the date the order was created/ placed
     * @param customer: the name of the person (customer) who made the order
     * @param creditCardNumber: the credit card number the customer
     *                         used to pay for the order
     * @param creditCardExpiry: the expiry date of the credit card
     *                         the customer used to pay for the order
     * @param cvv: the cvv security code number on the credit card
     *            the customer used to pay for the order
     * @param priceTotalInPence: the total price of all the items included
     *                          in the order
     * @param orderItems: a list of all the items included in the order
     */
    public Order(String orderNo, String orderDate, String customer,
                 String creditCardNumber, String creditCardExpiry,
                 String cvv, int priceTotalInPence, List<String> orderItems)
    {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
    }

    

    /**
     * computes the cost, in pence, of all items selected
     * from all the different restaurants, plus a charge of
     * £1 (100 pence) per delivery
     * @param participants an array of all existing restaurants
     *                     (from the REST server)
     * @param allPizzas a stream of pizzas to be ordered
     * @return the total cost (in pence) of all these items,
     * including delivery charges
     */
    public int getDeliveryCost(Restaurant[] participants, String... allPizzas) throws InvalidPizzaCombinationException {
        int totalInPence = 0;
        for (Restaurant participant : participants)
        {
            // get all menu items as Menu objects
            List<Menu> menuItems = Arrays.stream(participant.getMenu()).toList();
            // save the names of those Menu objects as a list of strings
            ArrayList<String> pizzas = new ArrayList<>(Arrays.stream(participant.getMenu()).map(Menu::name).toList());
            // store the stream of pizzas as a list
            List<String> orderedItems = List.of(allPizzas);
            if (pizzas.containsAll(orderedItems))
            {
                // if the pizza combination is valid, add the price
                // for each individual item to the total
                for (Menu item: menuItems)
                {
                    for (String orderItem: orderedItems)
                    {
                        if (item.name().equals(orderItem))
                        {
                            totalInPence += item.priceInPence();
                        }
                    }
                }
                // also include the £1 delivery fee
                return totalInPence + 100;
            }
        }
        // if no single restaurant can provide all pizzas, throw an exception
        throw new InvalidPizzaCombinationException("This pizza combination is invalid!");
    }

}
