package me.wondertwo.august0802.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import me.wondertwo.august0802.R;
import me.wondertwo.august0802.bean.girl.GirlItem;

/**
 * Created by wondertwo on 2016/8/3.
 */
public class GirlViewHolder extends BaseViewHolder<GirlItem> {

    ImageView itemPict;

    public GirlViewHolder(ViewGroup parent) {
        super(parent, R.layout.girl_item_layout);
        itemPict = $(R.id.item_iv);
    }

    @Override
    public void setData(GirlItem data) {
        super.setData(data);
        //Log.e("GirlViewHolder", data.url);
        Glide.with(getContext())
                .load(data.url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.glide_load_error)
                .into(itemPict);
    }
}
