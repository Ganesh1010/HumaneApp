package vuram_test_2.vuram.com.vuram_test_2;

public class NewNeedItemSpinnerData {

    String mainItemName;
    Integer mainItemImageId;
    public NewNeedItemSpinnerData(String mainItemName, Integer mainItemImageId){
        this.mainItemName=mainItemName;
        this.mainItemImageId=mainItemImageId;
    }

    public NewNeedItemSpinnerData(String mainItemName){
        this.mainItemName=mainItemName;
    }

    public String getMainItemName(){
        return mainItemName;
    }
    public Integer getMainItemImageId(){
        return mainItemImageId;
    }

}