package ca.mcgill.ecse321.utils;

/*
This class is instantiated to validate a password, which must contain between 8 and 20 characters and at least:
 a) 1 uppercase letter
 b) 1 lowercase letter
 c) 1 digit
 d) 1 special character
*/
public class PasswordValidator{

    public static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        boolean containsUpperCase = false;
        boolean containsLowerCase = false;
        boolean containsDigit = false;
        boolean containsSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                containsLowerCase = true;
            } else if (Character.isDigit(ch)) {
                containsDigit = true;
            } else if (isSpecialCharacter(ch)) {
                containsSpecialChar = true;
            }
        }

        return containsUpperCase && containsLowerCase && containsDigit && containsSpecialChar;
    }

    // We can define other special characters if we wish
    private static boolean isSpecialCharacter(char ch) {
        String specialCharacters = "!@#$%^&*()-_=+[]{};:'\"\\|,.<>/?";
        return specialCharacters.contains(Character.toString(ch));
    }

}
