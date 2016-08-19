package me.wondertwo.august0802.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.adapter.DoubanAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.douban.DoubanItem;
import me.wondertwo.august0802.bean.douban.DoubanPost;
import me.wondertwo.august0802.bean.douban.DoubanToday;
import me.wondertwo.august0802.ui.activity.DoubanActivity;
import me.wondertwo.august0802.util.Divider;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanFragment extends BaseFragment {

    private String TAG = "DoubanFragment";
    private Handler mHandler = new Handler();
    private DoubanAdapter mAdapter;

    private SimpleDateFormat format;
    private String mCurrentDate; // 当前日期，豆瓣一刻上线日期2014-05-12
    private int YEAR = 0;
    private int MONTH = 0;
    private int DAY = 0;
    private int loadCount = 1; // 记录加载次数

    @Bind(R.id.fragment_list_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fragment_list_recycler)
    EasyRecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化当前日期
        initCurrentDate();
    }

    private void initCurrentDate() {

        format = new SimpleDateFormat("yyyy-MM-dd"); // 定义日期格式

        // 日历日期：即当前日期前一月的今天，比如今天是20160818，则得到20160718
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);  // 2016
        MONTH = calendar.get(Calendar.MONTH); // 07
        DAY = calendar.get(Calendar.DAY_OF_MONTH); // 18

        // 得到当前日期，并格式化
        Date d = new Date(YEAR-1900, MONTH, DAY); // 得到2016-08-18
        mCurrentDate = format.format(d); // 2016-08-18

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        initRefreshLayout();

        return view;
    }

    private void initRecyclerView() {
        mAdapter = new DoubanAdapter(getActivity());
        configLatestAdapter(mAdapter);

        //设置LayoutManager
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //设置ItemDecoration
        mRecyclerView.addItemDecoration(new Divider(getActivity(), Divider.VERTICAL_LIST));
        //设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);

        //准备数据
        loadCurrentData(mCurrentDate);

    }

    private void initRefreshLayout() {

        //设置手指在屏幕上下拉多少距离开始刷新
        mRefreshLayout.setDistanceToTriggerSync(300);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        loadCurrentData(mCurrentDate);
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    /**
     * 设置 RecyclerArrayAdapter 处理滑动到底部加载更多、点击图片跳转Activity等逻辑
     */
    private void configLatestAdapter(final RecyclerArrayAdapter<DoubanItem> adapter) {
        mRecyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.recycler_load_more, this);
        adapter.setNoMore(R.layout.recycler_no_more);
        adapter.setError(R.layout.recycler_load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // handle the item click events
                Intent intent = new Intent(getActivity(), DoubanActivity.class);
                intent.putExtra("douban_id", adapter.getItem(position).id); //传递当前点击item的id
                intent.putExtra("douban_title", adapter.getItem(position).title);
                intent.putExtra("douban_image", adapter.getItem(position).image);
                intent.putExtra("douban_short_url", adapter.getItem(position).short_url);
                startActivity(intent);
            }
        });
    }

    private void loadCurrentData(String date) {
        unSubscribe();

        subscription = RetrofitClient.getDoubanTodayService().getDoubanTodaies(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DoubanToday>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "--------completed-------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "--------error-------");
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(DoubanToday doubanToday) {

                        List<DoubanPost> posts = doubanToday.posts;
                        List<DoubanItem> doubanItems = new ArrayList<>(posts.size());

                        for (DoubanPost post : posts) {

                            DoubanItem item = new DoubanItem();

                            // 列表展示数据
                            item.title = post.title;
                            // item.image = ?
                            if (!post.thumbs.isEmpty()) {
                                item.image = post.thumbs.get(0).medium.url;
                                //Log.e(TAG, "from thumbs: " + item.image);
                            } else if (post.author != null) {
                                item.image = post.author.large_avatar;
                                //Log.e(TAG, "from author: " + item.image);
                            }

                            // 跳转Activity传递数据
                            item.id = post.id;
                            item.short_url = post.short_url;
                            //item.share_pic_url = post.share_pic_url;
                            //item.like_count = post.like_count;
                            //item.comments_count = post.comments_count;

                            doubanItems.add(item);
                        }

                        mAdapter.addAll(doubanItems);
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void loadBeforeData() {
        Date d = new Date(YEAR - 1900, MONTH, DAY - loadCount); // 2016-08-17
        String date = format.format(d);
        loadCurrentData(date);
        loadCount++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    @Override
    public void onLoadMore() {
        loadBeforeData();
        super.onLoadMore();
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
