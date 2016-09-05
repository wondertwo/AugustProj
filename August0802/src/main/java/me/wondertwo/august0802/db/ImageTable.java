package me.wondertwo.august0802.db;

/**
 * Created by wondertwo on 2016/9/2.
 */
public class ImageTable {
    private int id;
    private String image_title;
    private String iamge_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }

    public String getIamge_url() {
        return iamge_url;
    }

    public void setIamge_url(String iamge_url) {
        this.iamge_url = iamge_url;
    }
}
