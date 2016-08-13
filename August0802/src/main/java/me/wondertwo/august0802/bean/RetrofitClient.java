package me.wondertwo.august0802.bean;

import me.wondertwo.august0802.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wondertwo on 2016/8/1.
 */
public class RetrofitClient {

    // 干货集中营妹纸图 api
    private static GirlService girlService;

    // 知乎日报最新更新 api
    private static DailyStoryService dailyStoryService;

    // 知乎日报文章内容 api
    private static ContentService contentService;

    // 知乎日报过往消息 api
    private static DailyBeforeService dailyBeforeService;


    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static OkHttpClient mClient = new OkHttpClient();


    // 封装 GirlService 请求
    public static GirlService getGirlService() {
        if (girlService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.GANK_GIRL_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            girlService = retrofit.create(GirlService.class);
        }
        return girlService;
    }

    // 封装 DailyStoryService 请求
    public static DailyStoryService getDailyStoryService() {
        if (dailyStoryService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.ZHIHU_NEWS_LATEST)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            dailyStoryService = retrofit.create(DailyStoryService.class);
        }
        return dailyStoryService;
    }

    // 封装 ContentService 请求
    public static ContentService getContentService() {
        if (contentService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.ZHIHU_NEWS_CONTENT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            contentService = retrofit.create(ContentService.class);
        }
        return contentService;
    }

    // 封装 DailyBeforeService 请求
    public static DailyBeforeService getDailyBeforeService() {
        if (dailyBeforeService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.ZHIHU_HISTORY_STORY)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            dailyBeforeService = retrofit.create(DailyBeforeService.class);
        }
        return dailyBeforeService;
    }


}
