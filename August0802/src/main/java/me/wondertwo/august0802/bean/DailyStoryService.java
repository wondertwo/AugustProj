package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.zhihu.DailyStory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/8.
 */
public interface DailyStoryService {

    @GET("latest")
    Observable<DailyStory> getDailyStories();

}
