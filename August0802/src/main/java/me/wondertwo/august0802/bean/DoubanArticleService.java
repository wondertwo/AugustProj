package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.douban.DoubanArticle;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/18.
 */
public interface DoubanArticleService {

    @GET("{id}")
    Observable<DoubanArticle> getDoubanArticles(@Path("id") long id);

}
