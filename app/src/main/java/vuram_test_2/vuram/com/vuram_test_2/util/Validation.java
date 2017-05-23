package vuram_test_2.vuram.com.vuram_test_2.util;


public class Validation {

    static Boolean validate = false;

    public static Boolean validate(String userLogin) {
        if (userLogin != null) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(userLogin).matches())
                validate = true;
            else if (android.text.TextUtils.isDigitsOnly(userLogin)) {
                if (userLogin.length() >= 4 && userLogin.length() <= 15)
                    validate = true;
                else
                    validate = false;


            } else
                validate = false;
        }
        return validate;
    }

    public static Boolean validate_email(String email){
        if(email!= null || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            validate = true;
        else
            validate = false;

        return validate;
    }
}


