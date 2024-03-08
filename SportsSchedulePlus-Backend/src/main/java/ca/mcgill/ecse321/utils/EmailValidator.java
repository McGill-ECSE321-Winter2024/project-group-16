package ca.mcgill.ecse321.utils;

import java.util.regex.Pattern;

import ca.mcgill.ecse321.SportsSchedulePlus.service.Mailer;


/**
 * Email Validator
 * @author Dania Bouhmidi
 */
public class EmailValidator {
private String email;
    
    
    public EmailValidator(String email) {
        this.email= email;
    }

    /**
     * Validates the input email
     * @return boolean
     */
    public boolean validate() {
        return !isBlank(email) && validFormat(email) && emailExists(email);
    }

    /**
     * Checks if the given string is blank
     * @param email
     * @return boolean
     */
    private boolean isBlank(String email) {
        if (email.isBlank()) {
           return true;
        }
        return false;
    }

    /**
     * Checks if the given input follows the proper email format
     * @param email
     * @return boolean
     */
    private boolean validFormat(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pattern = Pattern.compile(regex); 
        if(!pattern.matcher(email).matches()){
           return false;
        }
        return true;
    }
    /**
     * Checks if the input email exists
     * @param email
     * @return boolean
     */
    private boolean emailExists(String email){
        return Mailer.checkEmail(email);
    }
}
