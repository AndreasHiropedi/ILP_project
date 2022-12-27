package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the CreditCardValidator class
 * (ensures all validation methods in the class work as expected)
 */
public class TestCreditCardValidator
{

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    Order invalidOrder1 = new Order("0", "2023-02-26", "John", "2402902",
            "04/28", "922", 2400, new ArrayList<>());
    Order invalidOrder2 = new Order("1", "2023-02-26", "John", "4206755744907018",
            "04/12", "922", 2400, new ArrayList<>());
    Order invalidOrder3 = new Order("2", "2023-02-26", "John", "4206755744907018",
            "04/28", "2456", 2400, new ArrayList<>());
    Order invalidOrder4 = new Order("3", "2023-02-26", "John", "5423456",
            "04/28", "922", 2400, new ArrayList<>());
    Order invalidOrder5 = new Order("4", "2023-02-26", "John", "4206755744907018",
            "04/28", "9", 2400, new ArrayList<>());
    Order validCardDetailsOrder = new Order("5", "2023-02-26", "John", "4206755744907018",
            "04/28", "922", 2400, new ArrayList<>());
    CreditCardValidator validCardDetailsOrderValidator = new CreditCardValidator(validCardDetailsOrder);

    @Test
    @DisplayName("Testing if card number validation works")
    void testCardNumberValidation()
    {
        CreditCardValidator validator = new CreditCardValidator(invalidOrder1);
        assertFalse(validator.validCreditCardNumber());
        assertTrue(validCardDetailsOrderValidator.validCreditCardNumber());
    }

    @Test
    @DisplayName("Testing if card expiry date validation works")
    void testCardExpiryDateValidation()
    {
        CreditCardValidator validator = new CreditCardValidator(invalidOrder2);
        assertFalse(validator.validExpiryDate());
        assertTrue(validCardDetailsOrderValidator.validExpiryDate());
    }

    @Test
    @DisplayName("Testing if card cvv validation works")
    void testCardCvvValidation()
    {
        CreditCardValidator validator = new CreditCardValidator(invalidOrder3);
        assertFalse(validator.validCvv());
        assertTrue(validCardDetailsOrderValidator.validCvv());
    }

    @Test
    @DisplayName("Testing if card details validation works")
    void testCardDetailsValidation()
    {
        CreditCardValidator validator1 = new CreditCardValidator(invalidOrder4);
        CreditCardValidator validator2 = new CreditCardValidator(invalidOrder5);
        // check the order is labelled invalid, and outcome set accordingly
        assertFalse(validator1.validCreditCardDetails());
        assertEquals(OrderOutcome.InvalidCardNumber, invalidOrder4.getOutcome());
        // check the order is labelled invalid, and outcome set accordingly
        assertFalse(validator2.validCreditCardDetails());
        assertEquals(OrderOutcome.InvalidCvv, invalidOrder5.getOutcome());
        // check the order is labelled as having valid card details
        assertTrue(validCardDetailsOrderValidator.validCreditCardDetails());
    }

}
