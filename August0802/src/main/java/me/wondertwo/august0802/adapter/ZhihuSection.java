package me.wondertwo.august0802.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import me.wondertwo.august0802.R;

/**
 * Created by wondertwo on 2016/8/27.
 */
public class ZhihuSection extends BaseViewHolder<String> {

    private TextView tvSection;

    public ZhihuSection(ViewGroup parent) {
        super(parent, R.layout.global_item_section);
        tvSection = $(R.id.global_item_section);
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        tvSection.setText(data);
    }


}
