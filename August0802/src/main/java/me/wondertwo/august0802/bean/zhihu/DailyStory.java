package me.wondertwo.august0802.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 最新更新、过往消息的 Entity
 *
 * Created by wondertwo on 2016/8/8.
 */
public class DailyStory {

    // 定义序列化后的名称

    public @SerializedName("stories") List<Story> stories; // stories 结果集

    public static class Story {
        public String title;
        public List<String> images;
        public int type;
        public int id;
    }

}
