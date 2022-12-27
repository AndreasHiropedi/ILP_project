package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the CentralArea singleton class
 * (ensures all methods in the singleton class work as expected)
 */
public class TestCentralArea
{

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public TestCentralArea() throws MalformedURLException
    {

    }

    // =========================================================================
    // ================================ TESTS ==================================
    // =========================================================================

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // the base URL address (no extensions) for the REST server
    URL baseUrl = new URL("https://ilp-rest.azurewebsites.net");

    @Test
    @DisplayName("Testing if the central campus area is generated correctly")
    void testCentralAreaInstantiation()
    {
        List<LngLat> centralAreaVertices = CentralArea.getInstance(baseUrl).getCentralArea();
        assertEquals(4, centralAreaVertices.size());
    }

}
