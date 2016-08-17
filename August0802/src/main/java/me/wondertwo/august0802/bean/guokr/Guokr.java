package me.wondertwo.august0802.bean.guokr;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class Guokr {

    public @SerializedName("ok") Boolean response_ok;

    public @SerializedName("result") List<GuokrResult> response_results;

    public static class GuokrResult {
        public int id;
        public String title;

        public String headline_img_tb; // 用于文章列表页小图
        public String headline_img; // 用于文章内容页大图

        public String link;
        public String author;
        public String summary;
    }
}
