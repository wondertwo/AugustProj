package me.wondertwo.august0802.bean.guokr;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class GuokrArticle {

    public @SerializedName("ok") Boolean response_ok;

    public @SerializedName("result") List<GuokrArticleResult> response_result;


    public class GuokrArticleResult {

        public int id;

        public String title;

        public String content;

    }

}
