package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * this class is used to store all the information about orders
 * from the REST server, plus information about the outcome of each order
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

    // this field is used to update the status of each order
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

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the order number field
     * @return order number (as a string)
     */
    public String getOrderNo()
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
     * getter method for the customer field
     * @return customer (as a string)
     */
    public String getCustomer()
    {
        return customer;
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
     * getter method for the total price in pence field
     * (obtained from REST server)
     * @return total price in pence (as an int)
     */
    public int getPriceTotalInPence()
    {
        return priceTotalInPence;
    }

    /**
     * getter method for the order items field
     * @return order items (as a list of strings)
     */
    public List<String> getOrderItems()
    {
        return orderItems;
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
    // =========================== ORDER VALIDATION ============================
    // =========================================================================

    /**
     * validation method for the credit card number
     * Note: the implementation below is partly based on the following post
     * <a href = "https://howtodoinjava.com/java/regex/java-regex-validate-credit-card-numbers/">link</a>
     * @return true if the credit card number is valid,
     * false otherwise
     */
    public boolean validCreditCardNumber()
    {
        // cardValidationRegex that stores information about valid card numbers
        // based on different providers
        String cardValidationRegex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
                "(?<mastercard>5[1-5][0-9]{14})|" +
                "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
                "(?<amex>3[47][0-9]{13})|" +
                "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
                "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
        // create a pattern and matcher for the regex and card number
        // (to see if there is a match)
        Pattern pattern = Pattern.compile(cardValidationRegex);
        Matcher matcher = pattern.matcher(creditCardNumber);
        // this part is the Luhn algorithm for credit card number validation
        int luhnSum = 0;
        boolean shouldBeDoubled = false;
        // starting from last digit and going to the first
        for (int i = creditCardNumber.length() - 1; i >= 0; i--)
        {
            // get each individual digit from the card number
            int currentDigit = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            // check if it should be doubled, and if so, double it
            if (shouldBeDoubled)
            {
                currentDigit *= 2;
                // check the new result is a single digit number
                if (currentDigit > 9)
                {
                    // if not, convert it to a single digit number
                    currentDigit = (currentDigit % 10) + 1;
                }
            }
            // append the computed current digit to the sum of digits
            luhnSum += currentDigit;
            // and switch the value of the flag
            shouldBeDoubled = !shouldBeDoubled;
        }
        // if there is a match in terms of card pattern
        // and the Luhn checksum algorithm passes
        // then the credit card number is valid
        return matcher.matches() && (luhnSum % 10 == 0);
    }

    /**
     * validation method for the credit card expiry date
     * @return true if the credit card expiry is valid,
     * false otherwise
     */
    public boolean validExpiryDate()
    {
        // TODO
        return true;
    }

    /**
     * validation method for the credit card cvv number
     * @return true if the credit card cvv number is valid,
     * false otherwise
     */
    public boolean validCvv()
    {
        // TODO
        return true;
    }

    /**
     * check if all components of the card (i.e. card number,
     * expiry date, cvv) are valid, and update the order outcome
     * @return true if the card details are all valid, false otherwise
     */
    public boolean validCreditCardDetails()
    {
        if (!validCreditCardNumber())
        {
            outcome = OrderOutcome.InvalidCardNumber;
            return false;
        }
        else if (!validExpiryDate())
        {
            outcome = OrderOutcome.InvalidExpiryDate;
            return false;
        }
        else if (!validCvv())
        {
            outcome = OrderOutcome.InvalidCvv;
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean isOrderValid()
    {
        if (!validCreditCardDetails())
        {
            return false;
        }
        outcome = OrderOutcome.ValidButNotDelivered;
        return true;
    }

    // =========================================================================
    // ========================== OTHER CLASS METHODS ==========================
    // =========================================================================

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
    public int getDeliveryCost(Restaurant[] participants, String... allPizzas){
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
            }
        }
        // also include the £1 delivery fee
        return totalInPence + 100;
    }

}
