package vuram_test_2.vuram.com.vuram_test_2;

public class SpinnerItemData {

    String text;
    Integer imageId;
    public SpinnerItemData(String text, Integer imageId){
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