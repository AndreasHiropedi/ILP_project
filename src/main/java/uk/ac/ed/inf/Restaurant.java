package uk.ac.ed.inf;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * this class is used to store the details of
 * each restaurant on the PizzaDronz app, based on
 * the information from the REST server
 * @param name the name of the restaurant
 * @param menuItems all the items on the restaurant's menu
 * @param lng the longitude of the restaurant's location
 * @param lat the latitude of the restaurant's location
 */
public record Restaurant(
        String name,
        @JsonProperty("menu")
        List<Menu> menuItems,
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat)
{

    /**
     * this method is used to return all menu objects as an array
     * @return an array of all the items a restaurant has
     * on its menu (as menu objects)
     */
    public Menu[] getMenu()
    {
        return menuItems.toArray(new Menu[0]);
    }

    /**
     * returns an array of all participating restaurants (including their menus)
     * using the REST server
     * @param serverBaseAddress the URL address of the REST server
     * @return all existing restaurants, as an array of Restaurant objects
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress)
    {
        List<Restaurant> restaurants = RetrieveData.getInstance().retrieveRestaurantData(serverBaseAddress);
        return restaurants.toArray(new Restaurant[0]);
    }

}
