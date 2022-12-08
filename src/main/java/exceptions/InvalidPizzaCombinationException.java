package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the combination of order items is invalid
 * (no single provider can provide all the requested items)
 */
public class InvalidPizzaCombinationException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidPizzaCombinationException(String message)
    {
        super(message);
    }
}
