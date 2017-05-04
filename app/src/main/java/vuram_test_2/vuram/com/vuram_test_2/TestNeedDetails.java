package vuram_test_2.vuram.com.vuram_test_2;

import java.util.ArrayList;
import java.util.List;


public class TestNeedDetails{
    public int needId;
    public int orgId;
    public double Latitude;
    public double Longitude;

    public String orgName;
    public String orgAddress;
    public String orgContactNo;
    public String orgLogo;
    public int overallSatisfiedPercentage;
    public ArrayList<ItemDetails> itemDetailsList;

    public TestNeedDetails() {
        itemDetailsList = new ArrayList<ItemDetails>();
    }
}