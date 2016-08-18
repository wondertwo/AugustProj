package me.wondertwo.august0802.bean.douban;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wondertwo on 2016/8/18.
 */
public class DoubanPost {

    public @SerializedName("id") long id; // 文章ID

    public @SerializedName("title") String title; // 文章标题

    //public @SerializedName("url") String url; // 分享链接

    public @SerializedName("short_url") String short_url; // 分享短链接

    //public @SerializedName("share_pic_url") String share_pic_url; // 图片分享链接

    public @SerializedName("author") Author author; // 作者

    public @SerializedName("thumbs") List<PostThumbs> thumbs; // thumbs

    //public @SerializedName("like_count") String like_count; // 喜欢数

    //public @SerializedName("comments_count") String comments_count; // 评论数

    //public @SerializedName("published_time") String published_time; // 发布时间

    //public @SerializedName("created_time") String created_time; // 创建时间


    public class Author {

        public String large_avatar; // 图片URL

    }

}