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

    // a list of all restaurants on the PizzaDronz app
    private static Restaurant[] restaurants;

    // a list containing all orders that are deemed valid
    private static ArrayList<Order> validOrders = new ArrayList<>();

    // this field is used to store the delivery cost of an order
    // (including the £1 delivery fee)
    // set to 0 if the order is invalid
    private int orderDeliveryCost;

    // store the corresponding restaurant for the given order
    private Restaurant correspondingRestaurant;

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
     * getter method for the order number field
     * @return order number (as a string)
     */
    public String getOrderNumber()
    {
        return orderNo;
    }

    /**
     * getter method for the order date field
     * @return order date (as a string)
     */
    public String getOrderDate()
    {
        return orderDate;
    }

    /**
     * getter method for the order delivery cost field
     * @return the delivery cost of the order
     * (including the £1 delivery fee if the order is valid)
     */
    public int getOrderDeliveryCost()
    {
        return orderDeliveryCost;
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

    /**
     * getter method for the order's corresponding restaurant
     * @return restaurant to which the order was made
     */
    public Restaurant getCorrespondingRestaurant()
    {
        return correspondingRestaurant;
    }

    /**
     * getter method for the list of valid orders for
     * the inputted date (from the command line)
     * @return a list of all valid orders for a given date
     */
    public static ArrayList<Order> getValidOrders()
    {
        return validOrders;
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

    /**
     * setter method for the static restaurants list
     * based on URL inputted in the command line
     * @param restaurants the list of restaurants available
     *                    on the PizzaDronz app
     */
    public static void setRestaurants(Restaurant[] restaurants)
    {
        Order.restaurants = restaurants;
    }

    // =========================================================================
    // =========================== ORDER VALIDATION ============================
    // =========================================================================

    //
    // check if the order is valid, and update the order outcome accordingly
    // @return true if an order is valid, false otherwise
    private boolean isOrderValid()
    {
        // check if there are missing details, and if so, mark the order as invalid
        if (orderNo == null || customer == null)
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
        // if there are no or less than 1 order items,
        // or there are more than 4 items ordered
        // mark the order as invalid
        if (orderItems == null || orderItems.size() > 4 || orderItems.size() < 1)
        {
            outcome = OrderOutcome.InvalidPizzaCount;
            return false;
        }
        // update the delivery cost, and check if the order outcome has changed
        updateDeliveryCost();
        if (outcome != null)
        {
            // if the order outcome has changed, the order is invalid
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
     * plus a charge of £1 (100 pence) per delivery, and updates the
     * appropriate field
     * also updates the order outcome accordingly
     * (if an invalid pizza is found, if the pizzas cannot be provided
     * by a single supplier, or if the total is invalid)
     */
    public void updateDeliveryCost()
    {
        // this variable is used to keep track of the price
        // of all order items
        int totalInPence = 0;
        // keep track of all items in the order items list
        // that are defined
        int definedItems = 0;
        // keep track of all the items on the menu that match
        // the order items
        int itemsInMenu = 0;
        // this variable will be used to store the final delivery cost
        int deliveryCost = 0;
        // check to see if a single restaurant can deliver all the ordered items
        for (Restaurant restaurant : restaurants)
        {
            // get all menu items as Menu objects
            List<Menu> menuItems = restaurant.getMenuItems();
            for (Menu item: menuItems)
            {
                for (String orderItem: orderItems)
                {
                    // for each menu item, check if there is a match
                    if (orderItem.equals(item.name()))
                    {
                        // and update the defined items, items in menu
                        // and total price in pence
                        definedItems += 1;
                        itemsInMenu += 1;
                        totalInPence += item.priceInPence();
                    }
                }
                // if all order items are contained in the menu
                if (itemsInMenu == orderItems.size())
                {
                    // add the £1 delivery fee
                    deliveryCost = totalInPence + 100;
                    // set the corresponding restaurant field
                    correspondingRestaurant = restaurant;
                    // and check if the total is valid
                    if (deliveryCost != priceTotalInPence)
                    {
                        outcome = OrderOutcome.InvalidTotal;
                    }
                }
            }
            // if the menu doesn't contain all order items
            // reset the two fields
            itemsInMenu = 0;
            totalInPence = 0;
        }
        // if all items are defined, then set the outcome to invalid
        // due to multiple suppliers being needed
        if (definedItems == orderItems.size() && deliveryCost == 0)
        {
            outcome = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }
        // if not all items are defined, then set the outcome to invalid
        // due to an invalid pizza being found
        else if (definedItems != orderItems.size() && deliveryCost == 0)
        {
            outcome = OrderOutcome.InvalidPizzaNotDefined;
        }
        // set the order delivery cost field
        orderDeliveryCost = deliveryCost;
    }

    /**
     * this method is used to write the order objects
     * for a specific date (inputted in the command line)
     * to a JSON file
     */
    public void writeOrderToJson()
    {
        JsonFileWriter fileWriter = new JsonFileWriter();
        fileWriter.writeOrderToJSON(this);
    }

}
