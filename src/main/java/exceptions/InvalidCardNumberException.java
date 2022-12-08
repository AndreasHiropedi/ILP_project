package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the credit card number for an order is invalid
 */
public class InvalidCardNumberException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidCardNumberException(String message)
    {
        super(message);
    }

}
