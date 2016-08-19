package me.wondertwo.august0802.ui.fragment;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.adapter.GuokrAdapter;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.guokr.Guokr;
import me.wondertwo.august0802.bean.guokr.GuokrItem;
import me.wondertwo.august0802.ui.activity.GuokrActivity;
import me.wondertwo.august0802.util.Divider;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class GuokrFragment extends BaseFragment {

    private String TAG = "GuokrFragment";
    private GuokrAdapter mAdapter;

    @Bind(R.id.fragment_list_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fragment_list_recycler)
    EasyRecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        initRefreshLayout();

        return view;
    }

    private void initRecyclerView() {
        mAdapter = new GuokrAdapter(getActivity());
        configGuokrAdapter(mAdapter);

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
        loadGuokrData();

    }

    /**
     * 设置 RecyclerArrayAdapter 处理滑动到底部加载更多、点击图片跳转Activity等逻辑
     */
    private void configGuokrAdapter(final RecyclerArrayAdapter<GuokrItem> adapter) {
        mRecyclerView.setAdapterWithProgress(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // handle the item click events
                Intent intent = new Intent(getActivity(), GuokrActivity.class);
                intent.putExtra("article_image", adapter.getItem(position).headline_img);
                intent.putExtra("article_title", adapter.getItem(position).title);
                intent.putExtra("article_id", adapter.getItem(position).id); //传递当前点击item的文章id
                intent.putExtra("article_summary", adapter.getItem(position).summary); //传递当前点击item的文章id
                startActivity(intent);
            }
        });
    }

    // 准备数据
    private void loadGuokrData() {
        unSubscribe();

        // 默认加载最新的100条数据
        subscription = RetrofitClient.getGuokrService().getGuokrs("by_since", "all", 100, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Guokr>() {
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
                    public void onNext(Guokr guokr) {
                        if (guokr.response_ok) {
                            List<Guokr.GuokrResult> guokrResults = guokr.response_results;
                            List<GuokrItem> guokrItems = new ArrayList<>(guokrResults.size());
                            for (Guokr.GuokrResult result : guokrResults) {
                                GuokrItem item = new GuokrItem();
                                item.headline_img_tb = result.headline_img_tb;
                                item.title = result.title;
                                item.id = result.id;
                                item.headline_img = result.headline_img;
                                item.summary = result.summary;
                                guokrItems.add(item);
                            }
                            mAdapter.addAll(guokrItems);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("网络请求结果出了一点问题，请重试喔 ")
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
                                    }).create().show();
                        }
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
                        loadGuokrData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
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
