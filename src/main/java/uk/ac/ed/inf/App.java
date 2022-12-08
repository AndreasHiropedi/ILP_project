package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * this is the main project class, where everything comes together
 * (getting the necessary input from the command line,
 * validating that input, and obtaining the desired output
 * in the form of flight paths and order summaries as JSON and
 * GeoJSON files)
 */
public class App 
{

    // this method ensures the date inputted in the command line
    //is a valid date
    // @param dateToVerify the date to be used to filter out orders
    // @return a String of the provided date if valid
    private static String checkDateValid(String dateToVerify)
    {
        // check to avoid NullPointerException
        if (dateToVerify == null)
        {
            System.err.println("The date provided is invalid!");
            System.exit(1);
        }
        // check the provided date is in the appropriate format ("yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try
        {
            LocalDate.parse(dateToVerify,formatter);
        }
        catch (Exception e)
        {
            System.err.println("The date provided is in the wrong format!");
            System.exit(1);
        }
        LocalDate givenDate = LocalDate.parse(dateToVerify, formatter);
        // check provided date is in the appropriate range
        if ( givenDate.isBefore(LocalDate.parse("2023-01-01", formatter))
                || givenDate.isAfter(LocalDate.parse("2023-05-30", formatter)) )
        {
            System.err.println("The date provided is out of range!");
            System.exit(1);
        }
        return dateToVerify;
    }

     // this method ensures the URL inputted in the command line
     // is a valid URL
     // @param URLToVerify the URL inputted in the command line
     // @return a URL object of the provided URL if valid,
     // null otherwise
    private static URL checkURLValid(String URLToVerify)
    {
        // check to avoid NullPointerException
        if (URLToVerify == null)
        {
            System.err.println("The URL provided is invalid!");
            System.exit(1);
        }
        // check if the provided URL string ends with a '/'
        if (URLToVerify.endsWith("/"))
        {
            URLToVerify = URLToVerify.substring(0, URLToVerify.length() - 1);
        }
        // try creating a new URL object, and throw an error in case an exception occurs
        URL validatedURL = null;
        try
        {
            validatedURL = new URL(URLToVerify);
        }
        catch(MalformedURLException exception)
        {
            System.err.println("The URL provided is invalid!");
            System.exit(1);
        }
        return validatedURL;
    }

    /**
     * this is the main method, where the command line arguments
     * are taken as input, validated, and the whole algorithm for the
     * PizzaDronz app is executed
     * @param args the command line arguments passed
     *             into the program
     */
    public static void main(String[] args)
    {
        // check if enough arguments were given, to avoid any exceptions
        if (args.length < 2)
        {
            System.err.println("Not enough arguments were provided!");
            System.exit(1);
        }
        // retrieve the command line inputs
        String inputtedDate = args[0];
        String inputtedURL = args[1];
        // perform the validation checks for the command line inputs
        String validatedDate = checkDateValid(inputtedDate);
        URL validatedURL = checkURLValid(inputtedURL);
        // set the available restaurants field to all the restaurants from the validated URL
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(validatedURL);
        Order.setRestaurants(restaurants);
        // retrieve all available orders for the validated date
        String extension = "/orders" + "/" + validatedDate;
        List<Order> allOrders = RetrieveData.getData(validatedURL, extension, new TypeReference<>(){});
        // set up all the no-fly-zones
        extension = "/noFlyZones";
        List<NoFlyZone> allNoFlyZones = RetrieveData.getData(validatedURL, extension, new TypeReference<>(){});
        // set the base URL inside the LngLat class
        // (for retrieving data for the central area)
        LngLat.setBaseUrl(validatedURL);
        // create the drone
        Drone drone = new Drone();
        // set the date to be the validated date
        drone.setDateOfFlightPlan(validatedDate);
        // and run the flight planning algorithm for the given date
        drone.planFlightPath(allOrders);
    }
}
