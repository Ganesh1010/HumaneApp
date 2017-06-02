package vuram_test_2.vuram.com.vuram_test_2;

public class ItemSpinnerData {

    String text;
    Integer imageId;
    public ItemSpinnerData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public ItemSpinnerData(String text){
        this.text=text;
    }

    public String getText(){
        return text;
    }
    public Integer getImageId(){
        return imageId;
    }

}