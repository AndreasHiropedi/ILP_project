package uk.ac.ed.inf;

import java.net.URL;

/**
 * this singleton class is used to retrieve data
 * from the REST server
 */
public class RetrieveData
{
    private static RetrieveData retrieveData;

    public RetrieveData()
    {

    }

    /**
     *
     * @return
     */
    public static RetrieveData getInstance()
    {
        if (retrieveData == null)
        {
            retrieveData = new RetrieveData();
        }
        return retrieveData;
    }

    //TODO method for retrieving data (plus documentation)
    public static void FetchData(URL baseURL)
    {

    }

}
