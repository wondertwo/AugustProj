package me.wondertwo.august0802.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/10.
 */
public class Content {

    @SerializedName("title") public String title;

    @SerializedName("image") public String image;

    @SerializedName("share_url") public String share_url;

    @SerializedName("images") public List<String> images;

    @SerializedName("type") public int type;

    @SerializedName("id") public int id;

    @SerializedName("body") public String body;

    @SerializedName("css") public List<String> css;

}
