package vuram_test_2.vuram.com.vuram_test_2;


public class DataModel {


    String title;
    String requested;
    String donated;
    int id_;
    int image;

    public DataModel(int image,String title ,String requested,String donated,int id_) {
        this.title = title;
        this.requested=requested;
        this.donated=donated;
        this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return title;
    }
    public int getImage() {
        return image;
    }
    public String getRequested() {
    return requested;
}
    public String getdonated()
    {
        return donated;
    }
    public int getId() {
        return id_;
    }
}