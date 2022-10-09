package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * this class is used to store all orders based on the
 * information from the REST server
 * @param orderNo the unique number used to identify an order
 * @param orderDate the date the order was created/ placed
 * @param customer the name of the person (customer) who made the order
 * @param creditCardNumber the credit card number the customer
 *                         used to pay for the order
 * @param creditCardExpiry the expiry date of the credit card
 *                         the customer used to pay for the order
 * @param cvv the cvv security code number on the credit card
 *            the customer used to pay for the order
 * @param priceTotalInPence the total price of all the items included
 *                          in the order
 * @param orderItems a list of all the items included in the order
 */
public record Order(
        String orderNo,
        String orderDate,
        String customer,
        String creditCardNumber,
        String creditCardExpiry,
        String cvv,
        int priceTotalInPence,
        List<String> orderItems)
{

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
    public int getDeliveryCost(Restaurant[] participants, String... allPizzas)
    {
        try {
            int totalInPence = 0;
            for (Restaurant participant : participants) {
                List<Menu> menuItems = Arrays.stream(participant.getMenu()).toList();
                ArrayList<String> pizzas = new ArrayList<>(Arrays.stream(participant.getMenu()).map(Menu::name).toList());
                List<String> orderedItems = List.of(allPizzas);
                if (pizzas.containsAll(orderedItems))
                {
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
                    return totalInPence + 100;
                }
            }
            throw new InvalidPizzaCombinationException("This pizza combination is invalid!");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
