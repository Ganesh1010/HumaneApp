package vuram_test_2.vuram.com.vuram_test_2;

public class MainSpinnerItemData {

    String text;
    Integer imageId;
    public MainSpinnerItemData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }

}