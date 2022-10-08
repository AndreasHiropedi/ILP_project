package uk.ac.ed.inf;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

public class TestRestaurant
{

    public TestRestaurant() throws MalformedURLException
    {

    }

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    URL base = new URL("https://ilp-rest.azurewebsites.net");
    Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(base);

    @Test
    @DisplayName("Testing different scenarios for getMenu method")
    public void testGetMenu()
    {

    }

}
