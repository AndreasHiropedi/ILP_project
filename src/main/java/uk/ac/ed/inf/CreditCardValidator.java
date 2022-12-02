package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class is used to validate credit card information
 * for each order on the REST server
 */
public class CreditCardValidator
{
    // the order instance for which credit card validation will be performed
    private Order orderToVerify;

    /**
     * credit card validator class constructor
     * @param orderToVerify the order instance whose credit card information
     *                      will be verified
     */
    public CreditCardValidator(Order orderToVerify)
    {
        this.orderToVerify = orderToVerify;
    }

    /**
     * validation method for the credit card number
     * Note: the implementation below is partly based on the following post
     * <a href = "https://www.baeldung.com/java-validate-cc-number">link</a>
     * @return true if the credit card number is valid,
     * false otherwise
     */
    public boolean validCreditCardNumber()
    {
        String creditCardNumber = orderToVerify.getCreditCardNumber();
        // check if string is empty
        if (creditCardNumber == null)
        {
            return false;
        }
        // card validation regexes that stores information about valid card numbers
        // based on different providers
        String cardValidationVisa = "^4[0-9]{0,}$";
        String cardValidationMasterCard = "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]{0,}$";
        // create a pattern and matcher for the regexes and card number
        // (to see if there is a match)
        Pattern patternVisa = Pattern.compile(cardValidationVisa);
        Pattern patternMasterCard = Pattern.compile(cardValidationMasterCard);
        Matcher matcherVisa = patternVisa.matcher(creditCardNumber);
        Matcher matcherMasterCard = patternMasterCard.matcher(creditCardNumber);
        // this part is the Luhn algorithm for credit card number validation
        int luhnSum = 0;
        boolean shouldBeDoubled = false;
        // starting from last digit and going to the first
        for (int i = creditCardNumber.length() - 1; i >= 0; i--)
        {
            // get each individual digit from the card number
            try
            {
                int currentDigit = Integer.parseInt(creditCardNumber.substring(i, i + 1));
                // check if it should be doubled, and if so, double it
                if (shouldBeDoubled)
                {
                    currentDigit *= 2;
                    // check the new result is a single digit number
                    if (currentDigit > 9)
                    {
                        // if not, convert it to a single digit number
                        currentDigit = (currentDigit % 10) + 1;
                    }
                }
                // append the computed current digit to the sum of digits
                luhnSum += currentDigit;
                // and switch the value of the flag
                shouldBeDoubled = !shouldBeDoubled;
            }
            catch (Exception e)
            {
                System.out.println(creditCardNumber.substring(i, i + 1));
            }
            // if there is a match in terms of card pattern
            // and the Luhn checksum algorithm passes
            // then the credit card number is valid
            return (matcherVisa.matches() || matcherMasterCard.matches()) && (luhnSum % 10 == 0);
        }
        return false;
    }

    // given a month and a year (to account for leap years),
    // return the number of the last day of the given month
    // @param month the month number as a string
    // @param year the year number as a string
    // @return the last day of the given month as a string
    private String findLastDayOfMonth(String month, String year)
    {
        // check if the month is february and the year is a leap year
        if (month.equals("02") && Integer.parseInt(year) % 4 == 0)
        {
            return "29";
        }
        // check if the month is february (non-leap year)
        else if (month.equals("02"))
        {
            return "28";
        }
        // check if the month has 30 days (april, june, september, november)
        else if (month.equals("04") || month.equals("06") ||
                month.equals("09") || month.equals("11"))
        {
            return "30";
        }
        // if none of the other cases apply, the month must have 31 days
        return "31";
    }

    // convert a credit card expiry date (format mm/yy) into
    // a date in the format yyyy/mm/dd
    // (using the last day of the month for the dd part)
    // @return the newly formatted credit card expiry date
    // (as a string yyyy/mm/dd)
    private String convertExpiryDate()
    {
        String creditCardExpiry = orderToVerify.getCreditCardExpiry();
        //
        String new_date = "";
        // regex to check that the expiry date is in the format mm/yy
        String regex = "^((0[1-9])|(1[0-2]))/*((0[0-9])|([1-3][0-9]))$";
        // create a pattern matcher for the regex
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(creditCardExpiry);
        // check if the credit card expiry date matches the regex
        if (matcher.matches())
        {
            // if it does, break it down into year, month and day
            String year = "20" + creditCardExpiry.substring(3);
            String month = creditCardExpiry.substring(0, 2);
            String day = findLastDayOfMonth(month, year);
            // and assemble the newly formatted date string
            new_date = year + "-" + month + "-" + day;
        }
        return new_date;
    }

    /**
     * validation method for the credit card expiry date
     * @return true if the credit card expiry is valid,
     * false otherwise
     */
    public boolean validExpiryDate()
    {
        String creditCardExpiry = orderToVerify.getCreditCardExpiry();
        String orderDate = orderToVerify.getOrderDate();
        // check if either string is empty
        if (creditCardExpiry == null || orderDate == null)
        {
            return false;
        }
        // convert the order date to a date object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate orderDateObject = LocalDate.parse(orderDate, formatter);
        // convert the credit card expiry date into yyyy-mm-dd format
        String convertedDate = convertExpiryDate();
        // check if the returned string is empty
        if (convertedDate == null)
        {
            return false;
        }
        // if it's not, parse it as a date
        try {
            LocalDate expiryDateFormat = LocalDate.parse(convertedDate, formatter);
            // and check if the expiry date is not before the order date (making the expiry date valid)
            return !expiryDateFormat.isBefore(orderDateObject);
        }
        catch (Exception e){
            System.out.println(convertedDate);
        }
        return false;
    }

    /**
     * validation method for the credit card cvv number
     * @return true if the credit card cvv number is valid,
     * false otherwise
     */
    public boolean validCvv()
    {
        String cvv = orderToVerify.getCvv();
        // check if string is empty
        if (cvv == null)
        {
            return false;
        }
        // Regex to check for valid CVV number combinations
        String regex = "^[0-9]{3}$";
        // create a pattern and matcher for the regex and card number
        // (to see if there is a match)
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(cvv);
        // return true if there is a match between the cvv and the regex
        return matcher.matches();
    }

    /**
     * check if all components of the card (i.e. card number,
     * expiry date, cvv) are valid, and update the order outcome
     * @return true if the card details are all valid, false otherwise
     */
    public boolean validCreditCardDetails()
    {
        // if the credit card number is invalid, update the order outcome accordingly
        if (!validCreditCardNumber())
        {
            orderToVerify.setOutcome(OrderOutcome.InvalidCardNumber);
            return false;
        }
        // if the credit card expiry date is invalid, update the order outcome accordingly
        else if (!validExpiryDate())
        {
            orderToVerify.setOutcome(OrderOutcome.InvalidExpiryDate);
            return false;
        }
        // if the credit card cvv number is invalid, update the order outcome accordingly
        else if (!validCvv())
        {
            orderToVerify.setOutcome(OrderOutcome.InvalidCvv);
            return false;
        }
        return true;
    }

}