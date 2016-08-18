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
    private static DailyArticleService dailyArticleService;

    // 知乎日报过往消息 api
    private static DailyBeforeService dailyBeforeService;

    // 果壳精选文章列表 api
    private static GuokrService guokrService;

    // 果壳精选文章内容 api
    private static GuokrArticleService guokrArticleService;

    // 豆瓣一刻文章列表 api
    private static DoubanTodayService doubanTodayService;

    // 豆瓣一刻文章内容 api
    private static DoubanArticleService doubanArticleService;


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

    // 封装 DailyArticleService 请求
    public static DailyArticleService getDailyArticleService() {
        if (dailyArticleService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.ZHIHU_NEWS_CONTENT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            dailyArticleService = retrofit.create(DailyArticleService.class);
        }
        return dailyArticleService;
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

    // 封装 GuokrService 请求
    public static GuokrService getGuokrService() {
        if (guokrService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.GUOKR_NEWS_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            guokrService = retrofit.create(GuokrService.class);
        }
        return guokrService;
    }

    // 封装 GuokrArticleService 请求
    public static GuokrArticleService getGuokrArticleService() {
        if (guokrArticleService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.GUOKR_NEWS_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            guokrArticleService = retrofit.create(GuokrArticleService.class);
        }
        return guokrArticleService;
    }

    // 封装 DoubanTodayService 请求
    public static DoubanTodayService getDoubanTodayService() {
        if (doubanTodayService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.DOUBAN_MOMENT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            doubanTodayService = retrofit.create(DoubanTodayService.class);
        }
        return doubanTodayService;
    }

    // 封装 DoubanArticleService 请求
    public static DoubanArticleService getDoubanArticleService() {
        if (doubanArticleService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(Constants.DOUBAN_ARTICLE)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            doubanArticleService = retrofit.create(DoubanArticleService.class);
        }
        return doubanArticleService;
    }

}
