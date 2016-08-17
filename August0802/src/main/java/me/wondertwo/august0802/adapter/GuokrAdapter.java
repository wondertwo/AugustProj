package me.wondertwo.august0802.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import me.wondertwo.august0802.bean.guokr.GuokrItem;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class GuokrAdapter extends RecyclerArrayAdapter<GuokrItem> {

    public GuokrAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuokrViewHolder(parent);
    }
}
