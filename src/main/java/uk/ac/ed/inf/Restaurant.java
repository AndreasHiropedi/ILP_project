package uk.ac.ed.inf;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * this class is used to store the details of
 * each restaurant on the PizzaDronz app, based on
 * the information from the REST server
 */
public class Restaurant
{
    // all information from the REST server
    private String name;
    private List<Menu> menuItems;
    private double lng;
    private double lat;

    // field used to store a restaurant's location as a LngLat object
    private LngLat restaurantLocation;

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor for the restaurant objects
     * @param name the name of the restaurant (as a String)
     * @param menuItems a list of the restaurant's menu items
     *                  (stored as Menu objects)
     * @param lng the longitude of the restaurant's location
     * @param lat the latitude of the restaurant's location
     */
    public Restaurant(
            @JsonProperty("name")
            String name,
            @JsonProperty("menu")
            List<Menu> menuItems,
            @JsonProperty("longitude")
            double lng,
            @JsonProperty("latitude")
            double lat)
    {
        this.name = name;
        this.menuItems = menuItems;
        this.lng = lng;
        this.lat = lat;
        this.restaurantLocation = new LngLat(lng, lat);
    }

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the restaurant's name field
     * @return the restaurant's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * getter method for the restaurant's menu items
     * @return a list of the restaurant's menu items
     * (as Menu objects)
     */
    public List<Menu> getMenuItems()
    {
        return menuItems;
    }

    /**
     * getter method for the restaurant's location
     * @return the restaurant's location (as a LngLat object)
     */
    public LngLat getRestaurantLocation()
    {
        return restaurantLocation;
    }

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
     * @param baseURL the base URL for the REST server (validated from command line)
     * @return all existing restaurants, as an array of Restaurant objects
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL baseURL)
    {
        List<Restaurant> restaurants = RetrieveData.getData(baseURL, "/restaurants", new TypeReference<>(){});
        return restaurants.toArray(new Restaurant[0]);
    }

}
