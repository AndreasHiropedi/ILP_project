package uk.ac.ed.inf;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * this singleton class is used to retrieve data
 * from the REST server
 */
public class RetrieveData
{
    private static RetrieveData retrieveData;
    private static final String baseURL = "https://ilp-rest.azurewebsites.net";

    public RetrieveData()
    {

    }

    /**
     * creates an instance of the singleton class
     * @return the instance of the singleton object
     */
    public static RetrieveData getInstance()
    {
        if (retrieveData == null)
        {
            retrieveData = new RetrieveData();
        }
        return retrieveData;
    }

    /**
     *
     * @param baseURL
     * @param className
     * @return
     * @param <T>
     */
    public<T> ArrayList<T> getData(URL baseURL, Class<T> className)
    {
        String extension;
        switch (className.toString())
        {
            case "LngLat":
                extension = "/centralArea";
            case "Restaurant":
                extension = "/restaurants";
            case "Order":
                extension = "/orders";
            default:
                extension = "";
        }
        try
        {
            return new ObjectMapper().readValue(new URL(baseURL + extension), new TypeReference<>(){});
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieves the data for the Central Campus Area
     * from the REST server
     * @return an arraylist of the co-ordinates of the
     * Central Campus Area
     */
    public ArrayList<LngLat> retrieveCentralArea()
    {
        try
        {
            return new ObjectMapper().readValue(new URL(baseURL + "/centralArea"), new TypeReference<>(){});
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieves the data for all restaurants on the PizzaDronz
     * app from the REST server
     * @param baseURL the base URL which stores all restaurant data
     *                on the REST server
     * @return an arraylist of all the details for all
     * restaurants
     */
    public ArrayList<Restaurant> retrieveRestaurantData(URL baseURL)
    {
        try
        {
            return new ObjectMapper().readValue(new URL(baseURL.toString() + "/restaurants"), new TypeReference<>(){});
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieves the data for all orders made on the
     * PizzaDronz app from the REST server
     * @return an arraylist of all the details for all
     * orders placed
     */
    public ArrayList<Order> retrieveOrderData()
    {
        try
        {
            return new ObjectMapper().readValue(new URL(baseURL + "/orders"), new TypeReference<>(){});
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
