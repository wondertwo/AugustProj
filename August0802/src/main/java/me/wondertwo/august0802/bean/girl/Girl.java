package me.wondertwo.august0802.bean.girl;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Allenieo on 2016/8/1.
 */
public class Girl {

    public @SerializedName("results") List<GirlResult> girlResults; // 定义序列化后的名称

    public static class GirlResult {
        public String desc;
        public String url;
    }
}
