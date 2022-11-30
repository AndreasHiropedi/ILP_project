package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            LocalDateTime.parse(dateToVerify, formatter);
        }
        catch (Exception e)
        {
            System.err.println("The date provided is in the wrong format!");
            System.exit(1);
        }
        LocalDateTime givenDate = LocalDateTime.parse(dateToVerify, formatter);
        // check provided date is in the appropriate range
        if ( givenDate.isBefore(LocalDateTime.parse("2023-01-01", formatter))
                || givenDate.isAfter(LocalDateTime.parse("2023-05-31", formatter)) )
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
        // retrieve the command line inputs
        String inputtedDate = args[0];
        String inputtedURL = args[1];
        // perform the validation checks for the command line inputs
        String validatedDate = checkDateValid(inputtedDate);
        URL validatedURL = checkURLValid(inputtedURL);
        // set the available restaurants field to all the restaurants from the validated URL
        Order.setRestaurants(Restaurant.getRestaurantsFromRestServer(validatedURL));
        // TODO: link the validated date to the orders class

        // TODO: implement the rest of this method
    }

}
