package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.douban.DoubanToday;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/17.
 */
public interface DoubanTodayService {

    @GET("{date}")
    Observable<DoubanToday> getDoubanTodaies(@Path("date") String date);

}
