package me.wondertwo.august0802.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import me.wondertwo.august0802.R;
import me.wondertwo.august0802.bean.douban.DoubanItem;

/**
 * Created by wondertwo on 2016/8/17.
 */
public class DoubanViewHolder extends BaseViewHolder<DoubanItem> {

    private ImageView ivItemImage;
    private TextView tvItemTitle;

    public DoubanViewHolder(ViewGroup parent) {
        super(parent, R.layout.global_item_layout);
        ivItemImage = $(R.id.global_item_iv_image);
        tvItemTitle = $(R.id.global_item_tv_title);
    }

    @Override
    public void setData(DoubanItem data) {
        super.setData(data);
        Glide.with(getContext())
                .load(data.image)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.content_no_image)
                .error(R.drawable.content_no_image)
                .into(ivItemImage);
        tvItemTitle.setText(data.title);
    }
}
