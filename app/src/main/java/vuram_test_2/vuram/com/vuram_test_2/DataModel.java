package vuram_test_2.vuram.com.vuram_test_2;


public class DataModel {


    String title;
    int requested;
    int image;

    public DataModel(int image,String title ,int requested) {
        this.title = title;
        this.requested=requested;
        this.image=image;
    }

    public String getName() {
        return title;
    }
    public int getImage() {
        return image;
    }
    public int getRequested() {
        return requested;
    }
}