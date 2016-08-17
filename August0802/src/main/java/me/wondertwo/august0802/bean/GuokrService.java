package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.guokr.Guokr;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/17.
 */
public interface GuokrService {

    @GET("handpick/article.json")
    Observable<Guokr> getGuokrs(@Query("retrieve_type") String type,
                                @Query("category") String category,
                                @Query("limit") int limit,
                                @Query("ad") int ad);

}
