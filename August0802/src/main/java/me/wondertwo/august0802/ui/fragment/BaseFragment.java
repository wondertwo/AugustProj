package me.wondertwo.august0802.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import rx.Subscription;

/**
 * Created by wondertwo on 2016/8/3.
 */
public class BaseFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener {

    protected Subscription subscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();
    }

    protected void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
