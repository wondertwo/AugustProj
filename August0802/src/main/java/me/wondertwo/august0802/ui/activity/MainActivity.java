package me.wondertwo.august0802.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.ui.fragment.DoubanFragment;
import me.wondertwo.august0802.ui.fragment.GirlFragment;
import me.wondertwo.august0802.ui.fragment.GuokrFragment;
import me.wondertwo.august0802.ui.fragment.ZhihuFragment;
import me.wondertwo.august0802.util.NetStateUtils;
import me.wondertwo.august0802.util.StatusBarUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_ZHIHU = "ZhihuFragment";
    private static final String FRAGMENT_GIRL = "GirlFragment";
    private static final String FRAGMENT_GUOKR = "GuokrFragment";
    private static final String FRAGMENT_DOUBAN = "DoubanFragment";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.container_global)
    CoordinatorLayout coordinator;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.floating_bar)
    FloatingActionButton fab;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    private Fragment mFragment = null; //记录Activity当前持有的Fragment
    private int fc = R.id.container_content; //fragment container

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        ButterKnife.bind(this);
        StatusBarUtil.setColorForDrawerLayout(this, drawer, Color.parseColor("#373B3E"), 0);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "开启奇妙愉快的 [ 悦乎 ] 之旅吧", Snackbar.LENGTH_LONG).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        // 检查网络是否连接
        if (!NetStateUtils.isNetworkAvailable(this)) {
            Snackbar.make(coordinator, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (mFragment != null) {
                transaction.show(mFragment).commit();
            } else {
                mFragment = new ZhihuFragment(); //默认进入应用打开知乎
                transaction.add(fc, mFragment, FRAGMENT_ZHIHU).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_abouts) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Handle the action
        if (id == R.id.drawer_zhihu) {
            Fragment fragment = fm.findFragmentByTag(FRAGMENT_ZHIHU);
            if (fragment != null) {
                ft.hide(mFragment).show(fragment).commit();
            } else {
                fragment = new ZhihuFragment();
                ft.hide(mFragment).add(fc, fragment, FRAGMENT_ZHIHU).commit();
            }
            mFragment = fragment;
            toolbar.setTitle("知乎小报");
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.drawer_guokr) {
            Fragment fragment = fm.findFragmentByTag(FRAGMENT_GUOKR);
            if (fragment != null) {
                ft.hide(mFragment).show(fragment).commit();
            } else {
                fragment = new GuokrFragment();
                ft.hide(mFragment).add(fc, fragment, FRAGMENT_GUOKR).commit();
            }
            mFragment = fragment;
            toolbar.setTitle("果壳精选");
            fab.setVisibility(View.GONE);
        } else if (id == R.id.drawer_douban) {
            Fragment fragment = fm.findFragmentByTag(FRAGMENT_DOUBAN);
            if (fragment != null) {
                ft.hide(mFragment).show(fragment).commit();
            } else {
                fragment = new DoubanFragment();
                ft.hide(mFragment).add(fc, fragment, FRAGMENT_DOUBAN).commit();
            }
            mFragment = fragment;
            toolbar.setTitle("豆瓣一刻");
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.drawer_girl) {
            Fragment fragment = fm.findFragmentByTag(FRAGMENT_GIRL);
            if (fragment != null) {
                ft.hide(mFragment).show(fragment).commit();
            } else {
                fragment = new GirlFragment();
                ft.hide(mFragment).add(fc, fragment, FRAGMENT_GIRL).commit();
            }
            mFragment = fragment;
            toolbar.setTitle("漂亮妹纸");
            fab.setVisibility(View.GONE);
        } else if (id == R.id.drawer_share) {
            startActivity(new Intent(this, ShareActivity.class));
        } else if (id == R.id.drawer_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
