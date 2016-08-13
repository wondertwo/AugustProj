package me.wondertwo.august0802.ui.fragment;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.adapter.GirlAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.girl.Girl;
import me.wondertwo.august0802.bean.girl.GirlItem;
import me.wondertwo.august0802.ui.activity.GirlActivity;
import me.wondertwo.august0802.util.Dp2PxUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/4.
 */
public class GirlFragment extends BaseFragment {

    private String TAG = "GirlFragment";
    private Handler mHandler = new Handler();
    private int mRequestPage = 1;
    private GirlAdapter mAdapter;

    @Bind(R.id.fragment_girl_recycler)
    EasyRecyclerView mRecyclerView;
    /*@Bind(R.id.fragment_girl_refresh)
    SwipeRefreshLayout mRefreshLayout;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        mAdapter = new GirlAdapter(getActivity());
        doWithGirlAdapter(mAdapter);

        //设置LayoutManager
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        // 设置ItemDecoration
        SpaceDecoration itemDecoration = new SpaceDecoration((int) Dp2PxUtils.convertDpToPixel(8, getActivity()));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        mRecyclerView.addItemDecoration(itemDecoration);

        //设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshListener(this);

        //准备Girl数据，初始化RecyclerView的时候先只加载一页
        prepareGirlData(1);
    }

    /**
     * 设置 RecyclerArrayAdapter 处理滑动到底部加载更多、点击图片跳转Activity等逻辑
     */
    private void doWithGirlAdapter(final RecyclerArrayAdapter<GirlItem> adapter) {
        mRecyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.recycler_load_more, this);
        adapter.setNoMore(R.layout.recycler_no_more);
        adapter.setError(R.layout.recycler_load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), GirlActivity.class);
                intent.putExtra("desc",adapter.getItem(position).desc);
                intent.putExtra("url",adapter.getItem(position).url);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }

    // 准备Girl数据
    private void prepareGirlData(int page) {
        unSubscribe();

        Observer<List<GirlItem>> observer = new Observer<List<GirlItem>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("网络请求超时，请稍后重试 ")
                                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("好的好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                        Log.e(TAG, e.getMessage());
                    }
                    @Override
                    public void onNext(List<GirlItem> items) {
                        mAdapter.addAll(items);
                        mAdapter.notifyDataSetChanged();
                    }
                };
        subscription = loadFromNetwork(observer, page);

        Log.e(TAG, "subscribe successfully");
    }


    // 从网络加载数据
    public Subscription loadFromNetwork(@NonNull Observer<List<GirlItem>> observer, final int page) {

        return RetrofitClient.getGirlService().getGirls(10, page)
                .subscribeOn(Schedulers.io())
                // Observable返回类型GirlResult转换成List<GirlItem>
                .map(new Func1<Girl, List<GirlItem>>() {
                    @Override
                    public List<GirlItem> call(Girl girl) {
                        List<Girl.GirlResult> girls = girl.girlResults;
                        List<GirlItem> girlItems = new ArrayList<>(girls.size());
                        for (Girl.GirlResult result : girls) {
                            GirlItem girlItem = new GirlItem();
                            girlItem.desc = result.desc;
                            girlItem.url = result.url;
                            girlItems.add(girlItem);
                        }
                        return girlItems;
                    }
                })
                // 因为doOnNext()方法在onNext()方法前执行，两者间没有必然联系
                // 所以在doOnNext()方法中先把网络数据写入缓存
                .doOnNext(new Action1<List<GirlItem>>() {
                    @Override
                    public void call(List<GirlItem> girlItems) {
                        // 写入缓存
                        // DataCache.getInstance().writeItems(girlItems);
                        // Log.e("LoadingData", "data write in disk cache");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    /*@Override
    public void onRefresh() {

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.clear();
        prepareGirlData(mRequestPage);
    }*/

    @Override
    public void onLoadMore() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // before load more ,clean cache
                // LoadingData.getInstance().clearMemoryAndDiskCache();
                mRequestPage++;
                prepareGirlData(mRequestPage);
            }
        }, 1000);
    }


    /**
     * 重写以下两个方法，添加友盟统计
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
