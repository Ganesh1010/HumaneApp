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
    int id;
    boolean is_fulfilled;
    String latitude,longitude;
    List<NeedItemDetails> items;
    NeedDetails()
    {
        items=new ArrayList<>();
    }
    public  String  print()
    {
        return(items.get(0).print());
    }
}