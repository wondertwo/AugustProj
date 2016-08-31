package me.wondertwo.august0802.ui.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

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
import me.wondertwo.august0802.util.RecyclerDivider;
import me.wondertwo.august0802.util.TimeUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanFragment extends BaseFragment {

    private String TAG = "DoubanFragment";
    private DoubanAdapter mAdapter;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String mCurrentDate; // 当前日期，豆瓣一刻上线日期2014-05-12
    private int YEAR = 2014;
    private int MONTH = 5;
    private int DAY = 12;
    private int loadCount = 1; // 记录加载次数

    @Bind(R.id.fragment_list_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fragment_list_recycler)
    EasyRecyclerView mRecyclerView;
    @Bind(R.id.floating_bar)
    FloatingActionButton fab;
    @Bind(R.id.fragment_list_root)
    CoordinatorLayout root;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化当前日期
        initCurrentDate();
    }

    private void initCurrentDate() {
        // 日历日期：即当前日期前一月的今天，比如今天是20160818，则得到20160718
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);  // 2016
        MONTH = calendar.get(Calendar.MONTH); // 7
        DAY = calendar.get(Calendar.DAY_OF_MONTH); // 18

        mCurrentDate = TimeUtils.formatDate2(YEAR, MONTH, DAY); // 2016-08-18
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        initFloatingActionBtn();

        initRecyclerView();

        initRefreshLayout();

        return view;
    }

    private void initFloatingActionBtn() {
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                }, YEAR, MONTH, DAY);
                dpd.setTitle("选择日期以加载当天数据");

                final DatePicker picker = dpd.getDatePicker();
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadDataByDate(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                    }
                });
                dpd.show();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new DoubanAdapter(getActivity());
        configLatestAdapter(mAdapter);

        //设置LayoutManager
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //设置ItemDecoration
        mRecyclerView.addItemDecoration(new RecyclerDivider(getActivity(), RecyclerDivider.VERTICAL_LIST));
        //设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);

        //准备数据
        loadSpecifiedDate(mCurrentDate);

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
                        loadSpecifiedDate(mCurrentDate);
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

    private void loadSpecifiedDate(String date) {
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

    // 加载往期数据
    private void loadBeforeData() {
        Date d = new Date(YEAR - 1900, MONTH, DAY - loadCount); // 2016-08-17
        String date = format.format(d);
        loadSpecifiedDate(date);
        loadCount++;
    }

    // 选择日期加载数据，在MainActivity中调用
    public void loadDataByDate(int year, int month, int day) {
        //先进行日期范围的校验
        String specified = TimeUtils.formatDate2(year, month, day);
        Integer pick = Integer.parseInt(specified.replace("-", ""));
        Integer date = Integer.parseInt(mCurrentDate.replace("-", ""));

        if (pick < 20140512) {
            Snackbar.make(root, "请选择正确的日期，2014年05月12日至今天", Snackbar.LENGTH_LONG).show();
        } else if (pick > date) {
            Snackbar.make(root, "请选择正确的日期，2014年05月12日至今天", Snackbar.LENGTH_LONG).show();
        } else {
            //加载指定日期数据
            Snackbar.make(root, "当前加载的是" + year + "年" + (month + 1) + "月" + day + "日的数据", Snackbar.LENGTH_LONG).show();

            mAdapter.clear();
            loadSpecifiedDate(specified);
        }
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
