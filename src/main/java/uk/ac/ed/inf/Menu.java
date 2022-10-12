package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * this class is used to store the menu items
 * of a certain restaurant, based on the information
 * from the REST server
 * @param name the name of the item on the menu
 * @param priceInPence the price of that item, expressed in pence
 */
public record Menu(
        @JsonProperty("name")
        String name,
        @JsonProperty("priceInPence")
        int priceInPence)
{

}
