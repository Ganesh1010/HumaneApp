package vuram_test_2.vuram.com.vuram_test_2.util;


public class Validation {

    static boolean validate = false;

    public static boolean validate(String userLogin) {
        if (userLogin != null) {
            userLogin=userLogin.trim();
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

    public static boolean validate_email(String email){
        if(email!=null)
        {
           email=email.trim();
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validate = false;
        }
        else
            validate = true;
        return validate;
    }
}


