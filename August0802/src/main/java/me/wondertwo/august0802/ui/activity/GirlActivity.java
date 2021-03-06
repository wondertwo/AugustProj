package me.wondertwo.august0802.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.util.NetStateUtils;
import me.wondertwo.august0802.util.StatusBarUtil;

/**
 * Created by wondertwo on 2016/8/5.
 */
public class GirlActivity extends BaseActivity {


    @Bind(R.id.activity_girl)
    LinearLayout mLinearLayout;
    @Bind(R.id.girl_tool_btn)
    Toolbar mToolbar;
    @Bind(R.id.girl_picture_iv)
    ImageView mGirlPhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_dedtail);
        StatusBarUtil.setColor(this, Color.parseColor("#373B3E"), 0);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 检查网络，执行网络请求
        if (!NetStateUtils.isNetworkAvailable(this)) {
            Snackbar.make(mLinearLayout, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
        } else {
            Glide.with(this).load(getIntent().getStringExtra("url"))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.glide_load_error)
                    .into(mGirlPhoto);
            mGirlPhoto.setContentDescription(getIntent().getStringExtra("desc"));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.picture_save) {
            return true;
        } else if (id == R.id.picture_share) {
            return true;
        } else if (id == R.id.picture_favorite) {
            return true;
        } else if (id == R.id.picture_edit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    /**
     * 重写以下两个方法，添加友盟统计
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
