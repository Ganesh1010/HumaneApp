package vuram_test_2.vuram.com.vuram_test_2;

import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NeedDetails {
    int need_id;
    boolean is_fulfilled;
    String latitude, longitude;
    List<NeedItemDetails> items;
    List<DonationDetails> donations;
    OrganisationDetails org;

    NeedDetails() {
        items = new ArrayList<>();
        donations = new ArrayList<>();
    }

    public OrganisationDetails getOrganisationDetails(OrganisationDetails organisationDetails) {
        return organisationDetails;
    }

    public void setOrganisationDetails(OrganisationDetails organisationDetails)
    {
        this.org=organisationDetails;
    }

    public  String  print()
    {
        return(items.get(0).print());
    }
}