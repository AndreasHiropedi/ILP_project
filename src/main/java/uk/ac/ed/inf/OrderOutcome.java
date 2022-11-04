package uk.ac.ed.inf;

/**
 * this enum is used to store details regarding each
 * order outcome (whether it was delivered, valid but not delivered,
 * invalid, etc.)
 */
public enum OrderOutcome
{
    Delivered ,
    ValidButNotDelivered ,
    InvalidCardNumber ,
    InvalidExpiryDate ,
    InvalidCvv ,
    InvalidTotal ,
    InvalidPizzaNotDefined ,
    InvalidPizzaCount ,
    InvalidPizzaCombinationMultipleSuppliers ,
    Invalid
}

