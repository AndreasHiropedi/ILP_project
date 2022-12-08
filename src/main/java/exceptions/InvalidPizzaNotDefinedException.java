package exceptions;

/**
 * this exception class can be used to throw an exception
 * when any of the ordered items is not defined on any menu
 * of any participating restaurant on the app
 */
public class InvalidPizzaNotDefinedException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidPizzaNotDefinedException(String message)
    {
        super(message);
    }

}
