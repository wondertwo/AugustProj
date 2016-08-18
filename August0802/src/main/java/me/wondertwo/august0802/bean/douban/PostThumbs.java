package me.wondertwo.august0802.bean.douban;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wondertwo on 2016/8/18.
 */
public class PostThumbs {

    public @SerializedName("medium") ThumbsMedium medium; // medium


    public class ThumbsMedium {

        public String url; // 图片地址URL

    }

}
