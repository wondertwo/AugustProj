package me.wondertwo.august0802.cache;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.girl.Girl;
import me.wondertwo.august0802.bean.girl.GirlItem;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by wondertwo on 2016/8/3.
 */
public class LoadingData {

    private static final String TAG = "LoadGirlData";
    private static LoadingData instance;
    // 注意思考为什么用BehaviorSubject，而不是用Subscription ？？？
    BehaviorSubject<List<GirlItem>> cache;

    private LoadingData() {

    }

    // 单例
    public static LoadingData getInstance() {
        if (instance == null) {
            instance = new LoadingData();
        }
        return instance;
    }

    // 获取数据，先从内存加载，如果没有再从网络加载
    public Subscription subscribeData(@NonNull Observer<List<GirlItem>> observer, final int page) {
        if (cache == null ) {
            Log.e(TAG, "data cache is null");
            cache = BehaviorSubject.create();
        } else {
            Log.e(TAG, "memory has data, just read from memory");
        }

        return null;
    }

    // 释放内存缓存
    public void clearMemoryCache() {
        cache = null;
    }

    // 释放内存和磁盘缓存
    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        // 删除磁盘缓存
        DataCache.getInstance().deleteItems();
    }
}
