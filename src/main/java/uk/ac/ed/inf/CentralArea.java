package uk.ac.ed.inf;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * this singleton class is used strictly for
 * obtaining data regarding the Central Area
 * from the REST server
 */
public class CentralArea
{
    /**
     * the singleton instance that is only created once
     */
    private static CentralArea centralArea;
    /**
     * stores the list of the central area co-ordinates
     * as LngLat objects
     */
    private final List<LngLat> centralAreaVertices;

    /**
     * constructor method for the class
     * retrieve the data from the REST server inside constructor
     */
    public CentralArea() throws MalformedURLException
    {
        centralAreaVertices = RetrieveData.getData(
                new URL("https://ilp-rest.azurewebsites.net"),
                "/centralArea", new TypeReference<>(){});
    }

    /**
     * obtain an instance of the singleton class
     * @return an instance object of the CentralArea singleton
     */
    public static CentralArea getInstance() throws MalformedURLException
    {
        if (centralArea == null)
        {
            centralArea = new CentralArea();
        }
        return centralArea;
    }

    /**
     * getter for the list of central area co-ordinates
     * @return the list of central area co-ordinates
     * as LngLat objects
     */
    public List<LngLat> getCentralArea()
    {
        return centralAreaVertices;
    }

}
