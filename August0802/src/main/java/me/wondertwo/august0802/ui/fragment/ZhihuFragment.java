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
import me.wondertwo.august0802.adapter.ZhihuAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.zhihu.DailyStory;
import me.wondertwo.august0802.bean.zhihu.DailyStoryItem;
import me.wondertwo.august0802.ui.activity.DailyActivity;
import me.wondertwo.august0802.util.Divider;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/4.
 */
public class ZhihuFragment extends BaseFragment {

    private String TAG = "ZhihuFragment";
    private Handler mHandler = new Handler();
    private ZhihuAdapter mAdapter;

    // 2013年5月20日知乎日报上线
    private int YEAR = 2013;
    private int MONTH = 5;
    private int DAY = 20;
    // 记录当前加载历史消息的次数
    private int loadCount = -1;


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
        // 获取当前日期的前一天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-1);

        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
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
        mAdapter = new ZhihuAdapter(getActivity());
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
        loadLatestData();

    }


    /**
     * 设置 RecyclerArrayAdapter 处理滑动到底部加载更多、点击图片跳转Activity等逻辑
     */
    private void configLatestAdapter(final RecyclerArrayAdapter<DailyStoryItem> adapter) {
        mRecyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.recycler_load_more, this);
        adapter.setNoMore(R.layout.recycler_no_more);
        adapter.setError(R.layout.recycler_load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // handle the item click events
                Intent intent = new Intent(getActivity(), DailyActivity.class);
                intent.putExtra("story_id", adapter.getItem(position).id); //传递当前点击item的id
                intent.putExtra("story_title", adapter.getItem(position).title);
                intent.putExtra("story_image", adapter.getItem(position).images.get(0));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }

    // 准备数据
    private void loadLatestData() {
        unSubscribe();

        subscription = RetrofitClient.getDailyStoryService().getDailyStories()
                .subscribeOn(Schedulers.io())
                .map(new Func1<DailyStory, List<DailyStoryItem>>() {
                    @Override
                    public List<DailyStoryItem> call(DailyStory dailyStory) {
                        List<DailyStory.Story> stories = dailyStory.stories;

                        List<DailyStoryItem> dailyStoryItems = new ArrayList<>(stories.size());
                        for (DailyStory.Story result : stories) {
                            DailyStoryItem dailyStoryItem = new DailyStoryItem();

                            dailyStoryItem.title = result.title;
                            dailyStoryItem.images = result.images;
                            dailyStoryItem.type = result.type;
                            dailyStoryItem.id = result.id;

                            dailyStoryItems.add(dailyStoryItem);
                        }
                        Log.e(TAG, "map() dailyStory executed successfully");
                        return dailyStoryItems;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DailyStoryItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DailyStoryItem> zhihuStoryItems) {
                        mAdapter.addAll(zhihuStoryItems);
                        mAdapter.notifyDataSetChanged();
                    }
                });

        Log.e(TAG, "subscribe successfully");
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
                        loadLatestData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    // 加载往期新闻数据
    private void loadBeforeData() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date(YEAR-1900, MONTH, DAY - loadCount);
        final String date = format.format(d);

        unSubscribe();

        subscription = RetrofitClient.getDailyBeforeService().getBeforeStories(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyStory>() {
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
                    public void onNext(DailyStory dailyStory) {
                        List<DailyStory.Story> stories = dailyStory.stories;

                        List<DailyStoryItem> storyItems = new ArrayList<>(stories.size());
                        for (DailyStory.Story result : stories) {
                            DailyStoryItem item = new DailyStoryItem();
                            item.title = result.title;
                            item.images = result.images;
                            item.id = result.id;
                            item.type = result.type;

                            storyItems.add(item);
                        }

                        mAdapter.addAll(storyItems);
                        mAdapter.notifyDataSetChanged();

                        loadCount++;
                    }
                });
    }

    // long类型 date 转换为 String 类型
    private String parseDateToString(long currentDate) {

        Date date = new Date(currentDate + 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(date);

        return dateStr;
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
