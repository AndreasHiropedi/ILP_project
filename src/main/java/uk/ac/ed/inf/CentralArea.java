package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
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
    private List<LngLat> centralAreaVertices;

    /**
     * constructor method for the class
     * retrieve the data from the REST server inside constructor
     * @param baseURL the validated URL from the command line
     */
    public CentralArea(URL baseURL) {
        centralAreaVertices = RetrieveData.getData(baseURL, "/centralArea", new TypeReference<>(){});
    }

    /**
     * obtain an instance of the singleton class
     * @param baseURL the validated URL from the command line
     * @return an instance object of the CentralArea singleton
     */
    public static CentralArea getInstance(URL baseURL)
    {
        if (centralArea == null)
        {
            centralArea = new CentralArea(baseURL);
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
