package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by ganeshrajam on 17-04-2017.
 */

public class UserDetails {

    String first_name;
    String email;
    String password;
    RegisterDetails profile;

    public RegisterDetails getRegisterDetails() {
        return profile;
    }

    public void setRegisterDetails(RegisterDetails registerDetails) {
        this.profile = registerDetails;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirstname(String username) {
        this.first_name = username;
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

    public RegisterDetails getProfile() {
        return profile;
    }

    public void setProfile(RegisterDetails profile) {
        this.profile = profile;
    }
}
