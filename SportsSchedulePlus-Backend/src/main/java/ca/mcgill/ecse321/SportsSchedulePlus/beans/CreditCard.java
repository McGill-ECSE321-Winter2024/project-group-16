package ca.mcgill.ecse321.SportsSchedulePlus.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

/**
 * Class to represent a user's credit card
 *
 * @author Dania Bouhmidi
 */
public class CreditCard implements Serializable {

    private final Pattern visaPattern = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private final Pattern mastercardPattern = Pattern.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$");

    @NotNull(message = "{cardRequired}")
   
    @CreditCardNumber(ignoreNonDigitCharacters = true)
    private String number;

    @Future(message = "{expiredCard}")
    private Date expirationDate;

    /**
     * Credit card brand enum : the website only accepts VISA and MASTERCARD
     */
    public enum CardBrand {
        VISA, MASTERCARD, NONE
    }

    private CardBrand cardBrand;
    
    @NotNull(message = "{cardHolderRequired}")
    private String cardHolderName;

    /**
     * Default constructor : initializes string fields to empty strings
     */
    public CreditCard() {
        this.number = "";
        this.cardHolderName = "";
    }

    /**
     * Returns the cardholder's name
     *
     * @return cardHolderName
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * Sets the cardholder's name
     *
     * @param cardHolderName
     */
    public void setCardHolderName(final String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * Returns the expiration expirationDate of the credit card.
     *
     * @return expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the credit card number.
     */
    public void setNumber(final String number) {
        this.number = number;
        this.setCreditCardBrand();
    }

    /**
     * Sets the expiration expirationDate of the credit card.
     *
     * @param date
     */
    public void setExpirationDate(final Date date) {
        this.expirationDate = date;
    }

    /**
     * Sets the credit card number
     *
     * @param number
     */
    public CreditCard(final String number) {
        this.number = number;
    }

    /**
     * Returns the credit card number.
     *
     * @return number
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * Returns the card brand
     *
     * @return cardBrand
     */
    public CardBrand getCardBrand() {
        return cardBrand;
    }

    /**
     * Sets the card brand
     *
     * @param cardBrand
     */
    public void setCardBrand(CardBrand cardBrand) {
        this.cardBrand = cardBrand;
    }

    /**
     * Sets the credit card brand based on regex patterns.
     */
    private void setCreditCardBrand() {
        if (number != null && number.toString() != null && !number.toString().isEmpty()) {
            String formattedNumber = number.toString().trim().replaceAll("\\s", "");
            if (visaPattern.matcher(formattedNumber).matches()) {
                cardBrand = CardBrand.VISA;
            } else if (mastercardPattern.matcher(formattedNumber).matches()) {
                cardBrand = CardBrand.MASTERCARD;
            } else {
                cardBrand = CardBrand.NONE;
            }
        }
    }

    @Override
    /**
     * Returns the credit card number as the toString
     */
    public String toString() {
        return number;
    }
}

