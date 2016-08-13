package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.zhihu.DailyStory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/10.
 */
public interface DailyBeforeService {

    @GET("{date}")
    Observable<DailyStory> getBeforeStories(@Path("date") String date);

}
