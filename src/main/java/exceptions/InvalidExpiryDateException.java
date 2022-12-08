package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the credit card expiry date for an order is invalid
 */
public class InvalidExpiryDateException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidExpiryDateException(String message)
    {
        super(message);
    }

}
