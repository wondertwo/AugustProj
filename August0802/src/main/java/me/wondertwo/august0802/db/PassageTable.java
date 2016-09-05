package me.wondertwo.august0802.db;

/**
 * Created by wondertwo on 2016/9/2.
 */
public class PassageTable {
    private int id;
    private String passage_title;
    private String passage_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassage_title() {
        return passage_title;
    }

    public void setPassage_title(String passage_title) {
        this.passage_title = passage_title;
    }

    public String getPassage_url() {
        return passage_url;
    }

    public void setPassage_url(String passage_url) {
        this.passage_url = passage_url;
    }
}
