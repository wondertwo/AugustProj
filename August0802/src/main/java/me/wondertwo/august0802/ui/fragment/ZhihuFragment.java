package me.wondertwo.august0802.ui.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.adapter.ZhihuAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.zhihu.DailyStory;
import me.wondertwo.august0802.bean.zhihu.DailyStoryItem;
import me.wondertwo.august0802.ui.activity.DailyActivity;
import me.wondertwo.august0802.util.RecyclerDivider;
import me.wondertwo.august0802.util.TimeUtils;
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
    private int year = 2013;
    private int month = 05;
    private int day = 20;
    private int YEAR = 0;
    private int MONTH = 0;
    private int DAY = 0;
    // 记录当前加载历史消息的次数
    private int loadCount = -1;

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
        // 获取当前日期的前一天，比如现在是20160818
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // 把当前月份-1，得到20160717
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        YEAR = calendar.get(Calendar.YEAR);  // 2016
        MONTH = calendar.get(Calendar.MONTH); // 07
        DAY = calendar.get(Calendar.DAY_OF_MONTH); // 17
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
                }, year, month, day);
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
        mAdapter = new ZhihuAdapter(getActivity());
        configAdapter(mAdapter);

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
        loadLatestData();

    }


    /**
     * 设置 RecyclerArrayAdapter 处理滑动到底部加载更多、点击图片跳转Activity等逻辑
     */
    private void configAdapter(final RecyclerArrayAdapter<DailyStoryItem> adapter) {
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
                startActivity(intent);
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
                        // Log.e(TAG, "map() dailyStory executed successfully");
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
        loadSpecifiedDate(TimeUtils.formatDate1(YEAR, MONTH, DAY - loadCount));
        loadCount++;
    }

    // 选择日期加载数据，在MainActivity中调用
    public void loadDataByDate(int years, int months, int days) {

        //先进行日期范围的校验
        String specified = TimeUtils.formatDate1(years, months, days);
        Integer pick = Integer.parseInt(specified);
        Integer date = Integer.parseInt(TimeUtils.formatDate1(year, month, day));

        if (pick < 20130520) {
            Snackbar.make(root, "请选择正确的日期，2013年05月20日至今天", Snackbar.LENGTH_LONG).show();
        } else if (pick > date) {
            Snackbar.make(root, "请选择正确的日期，2013年05月20日至今天", Snackbar.LENGTH_LONG).show();
        } else {
            //加载指定日期数据
            Snackbar.make(root, "当前加载的是" + years + "年" + (months + 1) + "月" + days + "日的数据", Snackbar.LENGTH_LONG).show();

            mAdapter.clear();
            if (pick == date) {
                loadLatestData();
            } else {
                loadSpecifiedDate(specified);
            }
        }
    }

    // 加载指定日期数据
    public void loadSpecifiedDate(String date) {
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
                    }
                });
    }

    // long类型 date 转换为 String 类型
    /*private String parseDateToString(long currentDate) {

        Date date = new Date(currentDate + 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(date);

        return dateStr;
    }*/

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
