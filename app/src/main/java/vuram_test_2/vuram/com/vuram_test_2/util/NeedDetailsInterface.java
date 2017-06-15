package vuram_test_2.vuram.com.vuram_test_2.util;

import java.util.ArrayList;

import vuram_test_2.vuram.com.vuram_test_2.NeedDetails;

/**
 * Created by ganeshrajam on 15-06-2017.
 */

public interface NeedDetailsInterface {
    public abstract void setNeedDetailsinActivity(ArrayList<NeedDetails> needDetails);
    public abstract void printNeedDetails();
    public abstract ArrayList<NeedDetails> getNeedItemFromActivity();
}
