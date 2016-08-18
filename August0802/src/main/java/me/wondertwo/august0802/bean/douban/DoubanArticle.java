package me.wondertwo.august0802.bean.douban;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/18.
 */
public class DoubanArticle {

    public @SerializedName("content") String content;

    public @SerializedName("photos") List<ArticlePhotos> photos;

}
