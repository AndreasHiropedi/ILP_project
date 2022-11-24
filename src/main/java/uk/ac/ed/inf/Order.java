package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * this class is used to store all the information about orders
 * from the REST server, plus information about the outcome of each order
 */
public class Order
{
    // all the information from the REST server
    private String orderNo;
    private String orderDate;
    private String customer;
    private String creditCardNumber;
    private String creditCardExpiry;
    private String cvv;
    private int priceTotalInPence;
    private List<String> orderItems;

    // this field is used to update the status of each order
    private OrderOutcome outcome;

    // TODO: define the restaurants list once REST retrieval is fixed
    // a list of all restaurants on the PizzaDronz app
    public static Restaurant[] restaurants;

    // a list containing all orders that are deemed valid
    public static ArrayList<Order> validOrders;

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

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
    public Order(
            @JsonProperty("orderNo")
            String orderNo,
            @JsonProperty("orderDate")
            String orderDate,
            @JsonProperty("customer")
            String customer,
            @JsonProperty("creditCardNumber")
            String creditCardNumber,
            @JsonProperty("creditCardExpiry")
            String creditCardExpiry,
            @JsonProperty("cvv")
            String cvv,
            @JsonProperty("priceTotalInPence")
            int priceTotalInPence,
            @JsonProperty("orderItems")
            List<String> orderItems)
    {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
        // if the order is valid, add it to the list of valid orders
        if (isOrderValid())
        {
            validOrders.add(this);
        }
    }

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the order date field
     * @return order date (as a string)
     */
    public String getOrderDate()
    {
        return orderDate;
    }

    /**
     * getter method for the credit card number field
     * @return credit card number (as a string)
     */
    public String getCreditCardNumber()
    {
        return creditCardNumber;
    }

    /**
     * getter method for the credit card expiry date field
     * @return credit card expiry date (as a string)
     */
    public String getCreditCardExpiry()
    {
        return creditCardExpiry;
    }

    /**
     * getter method for the credit card cvv number field
     * @return cvv number (as a string)
     */
    public String getCvv()
    {
        return cvv;
    }

    /**
     * getter method for the order outcome field
     * @return outcome of the order
     */
    public OrderOutcome getOutcome()
    {
        return outcome;
    }

    // =========================================================================
    // ================================ SETTERS ================================
    // =========================================================================

    /**
     * setter method for the order outcome field
     * @param outcome the new outcome value that the class field will store
     */
    public void setOutcome(OrderOutcome outcome)
    {
        this.outcome = outcome;
    }

    // =========================================================================
    // =========================== ORDER VALIDATION ============================
    // =========================================================================

    /**
     * check if the order is valid, and update the order outcome accordingly
     * @return true if an order is valid, false otherwise
     */
    public boolean isOrderValid()
    {
        // check if there are missing details, and if so, mark the order as invalid
        if (orderNo == null || orderDate == null || customer == null)
        {
            outcome = OrderOutcome.Invalid;
            return false;
        }
        // check if the credit card details are valid, and update the outcome accordingly
        CreditCardValidator creditCardValidator = new CreditCardValidator(this);
        if (!creditCardValidator.validCreditCardDetails())
        {
            return false;
        }
        // if there are no order items, or there are more than 4 items ordered
        // mark the order as invalid
        if (orderItems == null || orderItems.size() > 4)
        {
            outcome = OrderOutcome.InvalidPizzaCount;
            return false;
        }
        // check if the delivery cost matches the total stored on the REST server
        if (getDeliveryCost() != priceTotalInPence && outcome == null)
        {
            outcome = OrderOutcome.InvalidTotal;
            return false;
        }
        // if all checks pass, then the order is valid
        outcome = OrderOutcome.ValidButNotDelivered;
        return true;
    }

    // =========================================================================
    // ========================== OTHER CLASS METHODS ==========================
    // =========================================================================

    /**
     * computes the cost, in pence, of all order items
     * plus a charge of £1 (100 pence) per delivery
     * @return the total cost (in pence) of all these items,
     * including delivery charges
     */
    public int getDeliveryCost(){
        int totalInPence = 0;
        for (Restaurant participant : restaurants)
        {
            // get all menu items as Menu objects
            List<Menu> menuItems = Arrays.stream(participant.getMenu()).toList();
            // save the names of those Menu objects as a list of strings
            ArrayList<String> pizzas = new ArrayList<>(Arrays.stream(participant.getMenu()).map(Menu::name).toList());
            if (pizzas.containsAll(orderItems))
            {
                // add the price for each individual item to the total
                for (Menu item: menuItems)
                {
                    for (String orderItem: orderItems)
                    {
                        if (item.name().equals(orderItem))
                        {
                            totalInPence += item.priceInPence();
                        }
                    }
                }
            }
        }
        // include the £1 delivery fee
        return totalInPence + 100;
    }

}
