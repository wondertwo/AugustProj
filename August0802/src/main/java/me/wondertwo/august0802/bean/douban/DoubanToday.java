package me.wondertwo.august0802.bean.douban;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanToday {

    public @SerializedName("total") int total;

    public @SerializedName("date") String date;

    public @SerializedName("posts") List<DoubanPost> posts;

}
