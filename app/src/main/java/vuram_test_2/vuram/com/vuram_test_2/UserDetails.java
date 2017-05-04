package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by ganeshrajam on 17-04-2017.
 */

public class UserDetails {
    public RegisterDetails getRegisterDetails() {
        return profile;
    }

    public void setRegisterDetails(RegisterDetails registerDetails) {
        this.profile = registerDetails;
    }


    public UserDetails()
    {
        profile=new RegisterDetails();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String username,email,password;
    RegisterDetails profile;
}
