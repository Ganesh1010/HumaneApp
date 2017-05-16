package vuram_test_2.vuram.com.vuram_test_2;


import java.util.Date;

/**
 * Created by ganeshrajam on 17-04-2017.
 */

public class RegisterDetails {
   String mobile,gender;
    int country;
    Date dob;
    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }


}
