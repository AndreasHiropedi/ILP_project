package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the credit card cvv number for an order is invalid
 */
public class InvalidCvvException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidCvvException(String message)
    {
        super(message);
    }

}
