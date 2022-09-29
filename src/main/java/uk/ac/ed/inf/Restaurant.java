package uk.ac.ed.inf;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Restaurant(
        String name,
        @JsonProperty("menu")
        List<Menu> menuItems,
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat
)
{

    /**
     *
     * @return
     */
    public Menu[] getMenu()
    {
        return menuItems.toArray(new Menu[0]);
    }

    /**
     *
     * @param serverBaseAddress
     * @return
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress)
    {
        List<Restaurant> restaurants = null;
        return restaurants.toArray(new Restaurant[0]);
    }

}
