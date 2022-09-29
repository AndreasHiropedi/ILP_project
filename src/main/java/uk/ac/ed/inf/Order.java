package uk.ac.ed.inf;

import java.util.List;
import java.util.Vector;

public record Order(
        String orderNo,
        String orderDate,
        String customer,
        String creditCardNumber,
        String creditCardExpiry,
        String cvv,
        int priceTotalInPence,
        List<String> orderItems
)
{

    public int getDeliveryCost(Vector<Restaurant> restaurants, List<String> allPizzas)
    {
        // TODO
        return 0;
    }

}
