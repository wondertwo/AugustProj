package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.zhihu.Content;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/10.
 */
public interface ContentService {

    @GET("{id}")
    Observable<Content> getContent(@Path("id") int id);

}
