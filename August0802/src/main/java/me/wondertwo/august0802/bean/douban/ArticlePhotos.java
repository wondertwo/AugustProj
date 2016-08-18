package me.wondertwo.august0802.bean.douban;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wondertwo on 2016/8/18.
 */
public class ArticlePhotos {

    public @SerializedName("medium") PhotosMedium medium;

    public @SerializedName("tag_name") String tag_name;

    public class PhotosMedium {

        public String url; // 图片地址URL

    }

}
