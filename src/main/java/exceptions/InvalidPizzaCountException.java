package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the number of items ordered exceeds the maximum 4 pizzas
 * per order that is allowed, or contains no pizzas
 */
public class InvalidPizzaCountException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidPizzaCountException(String message)
    {
        super(message);
    }

}
