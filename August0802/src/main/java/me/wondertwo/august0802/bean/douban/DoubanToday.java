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


    public class DoubanPost {

        public String id; // 文章ID

        public String title; // 文章标题

        public String url; // 分享链接

        public String short_url; // 分享短链接

        public String share_pic_url; // 图片分享链接

        public String like_count; // 喜欢数

        public String comments_count; // 评论数

        public String published_time; // 发布时间

        public String created_time; // 创建时间

        /*public class Author {

            public String large_avatar; // 头像大图

        }*/

    }
}
