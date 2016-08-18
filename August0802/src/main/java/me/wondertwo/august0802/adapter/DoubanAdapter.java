package me.wondertwo.august0802.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import me.wondertwo.august0802.bean.douban.DoubanItem;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanAdapter extends RecyclerArrayAdapter<DoubanItem> {

    public DoubanAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoubanViewHolder(parent);
    }
}
