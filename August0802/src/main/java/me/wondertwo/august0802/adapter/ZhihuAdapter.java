package me.wondertwo.august0802.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import me.wondertwo.august0802.bean.zhihu.DailyStoryItem;

/**
 * Created by wondertwo on 2016/8/8.
 */
public class ZhihuAdapter extends RecyclerArrayAdapter<DailyStoryItem> {

    public ZhihuAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZhihuViewHolder(parent);
    }

//    @Override
//    public void OnBindViewHolder(BaseViewHolder holder, int position) {
//        super.OnBindViewHolder(holder, position);
//    }
//
//    @Override
//    public int getViewType(int position) {
//        return super.getViewType(position);
//    }
}
