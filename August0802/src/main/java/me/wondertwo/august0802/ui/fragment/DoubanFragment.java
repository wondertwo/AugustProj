package me.wondertwo.august0802.ui.fragment;

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

import com.google.gson.stream.JsonReader;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.adapter.DoubanAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.douban.DoubanPost;
import me.wondertwo.august0802.bean.douban.DoubanToday;
import me.wondertwo.august0802.bean.douban.DoubanItem;
import me.wondertwo.august0802.bean.douban.PostThumbs;
import me.wondertwo.august0802.util.Divider;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanFragment extends BaseFragment {

    private String TAG = "DoubanFragment";
    private Handler mHandler = new Handler();
    private DoubanAdapter mAdapter;
    private String mDate; // 当前日期
    // 记录当前加载历史消息的次数
    private int loadCount = -1;
    private int YEAR = 0;
    private int MONTH = 0;
    private int DAY = 0;

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
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
        mDate = "" + YEAR + "-" + MONTH + "-" + DAY;
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
        loadCurrentData(mDate);

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
                /*Intent intent = new Intent(getActivity(), DailyActivity.class);
                intent.putExtra("story_id", adapter.getItem(position).id); //传递当前点击item的id
                intent.putExtra("story_title", adapter.getItem(position).title);
                intent.putExtra("story_image", adapter.getItem(position).images.get(0));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());*/
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

                            item.id = post.id;
                            item.title = post.title;
                            item.url = post.url;

                            // item.image = ?
                            if (post.thumbs != null) {
                                List<PostThumbs> thumbs = post.thumbs;
                                PostThumbs.ThumbsMedium medium = thumbs.get(0).medium;
                                item.image = medium.url;
                            } else if (post.author != null) {
                                DoubanPost.Author author = post.author;
                                item.image = author.large_avatar;
                                Log.e(TAG, item.image);
                            }
                            doubanItems.add(item);
                        }

                        mAdapter.addAll(doubanItems);
                        mAdapter.notifyDataSetChanged();

                    }
                });
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
                        loadCurrentData(mDate);
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void loadBeforeData() {
        String date = "" + (YEAR-1900) + "-" + MONTH + "-" + (DAY-loadCount);
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

}
