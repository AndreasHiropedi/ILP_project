package exceptions;

/**
 * this exception class can be used to throw an exception
 * when any of the order details are invalid (e.g. details are null,
 * empty strings, etc.)
 */
public class InvalidOrderException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidOrderException(String message)
    {
        super(message);
    }

}
