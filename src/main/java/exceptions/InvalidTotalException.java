package exceptions;

/**
 * this exception class can be used to throw an exception
 * when the computed total for the order stored on the REST server
 * does not match the actual delivery cost
 */
public class InvalidTotalException extends Exception
{

    /**
     * constructor method for the exception
     * @param message the text message to be displayed
     */
    public InvalidTotalException(String message)
    {
        super(message);
    }

}
