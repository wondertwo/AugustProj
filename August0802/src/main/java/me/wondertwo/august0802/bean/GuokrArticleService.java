package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.guokr.GuokrArticle;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/17.
 */
public interface GuokrArticleService {

    @GET("handpick/article.json")
    Observable<GuokrArticle> getGuokrArticles(@Query("pick_id") int pick_id);

}
