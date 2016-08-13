package me.wondertwo.august0802.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import me.wondertwo.august0802.R;
import me.wondertwo.august0802.bean.zhihu.DailyStoryItem;

/**
 * Created by wondertwo on 2016/8/8.
 */
public class ZhihuViewHolder extends BaseViewHolder<DailyStoryItem> {

    private ImageView ivItemImage;
    private TextView tvItemTitle;

    public ZhihuViewHolder(ViewGroup parent) {
        super(parent, R.layout.global_item_layout);
        ivItemImage = $(R.id.global_item_iv_image);
        tvItemTitle = $(R.id.global_item_tv_title);
    }

    @Override
    public void setData(DailyStoryItem data) { //data的images字段是一个数组，我们只取第一个值
        super.setData(data);
        //Log.e("ZhihuViewHolder", data.images.get(0));
        Glide.with(getContext())
                .load(data.images.get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivItemImage);
        tvItemTitle.setText(data.title);
    }
}
