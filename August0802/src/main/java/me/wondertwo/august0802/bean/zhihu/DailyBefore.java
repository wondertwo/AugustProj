package me.wondertwo.august0802.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/11.
 */
public class DailyBefore {

    public @SerializedName("date") long date; // 日期

    public @SerializedName("stories") List<Story> stories; // stories 结果集

    public static class Story {
        public String title;
        public List<String> images;
        public int type;
        public int id;
    }

}
