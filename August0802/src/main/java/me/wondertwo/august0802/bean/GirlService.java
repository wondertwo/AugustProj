package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.bean.girl.Girl;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wondertwo on 2016/8/1.
 */
public interface GirlService {

    @GET("{number}/{page}")
    Observable<Girl> getGirls(@Path("number") int number, @Path("page") int page);

}
