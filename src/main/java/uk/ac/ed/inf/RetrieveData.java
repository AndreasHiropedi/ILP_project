package uk.ac.ed.inf;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;

/**
 * this class is used to retrieve data
 * from the REST server
 */
public class RetrieveData
{
    /**
     * generic method for retrieving the data from the REST server
     * for each individual class
     * @param baseURL the base REST server
     * @param extension the necessary extension (depends on the class)
     * @param typeReference the type of the object (or list of objects) to be returned
     * @param <T> allows for multiple classes to use this method
     * @return the data from the REST server in the specified format
     */
    public static <T> T getData(URL baseURL, String extension, TypeReference<T> typeReference)
    {
        try
        {
            return new ObjectMapper().readValue(new URL(baseURL.toString() + extension),
                    typeReference);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
