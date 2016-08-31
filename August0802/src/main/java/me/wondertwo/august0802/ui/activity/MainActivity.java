package me.wondertwo.august0802.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import me.wondertwo.august0802.util.NetStateUtils;
import me.wondertwo.august0802.util.StatusBarUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_ZHIHU = "ZhihuFragment";
    private static final String FRAGMENT_GIRL = "GirlFragment";
    private static final String FRAGMENT_GUOKR = "GuokrFragment";
    private static final String FRAGMENT_DOUBAN = "DoubanFragment";

    private String fragment_zhihu = "me.wondertwo.august0802.ui.fragment.ZhihuFragment";
    private String fragment_guokr = "me.wondertwo.august0802.ui.fragment.GuokrFragment";
    private String fragment_douban = "me.wondertwo.august0802.ui.fragment.DoubanFragment";
    private String fragment_girl = "me.wondertwo.august0802.ui.fragment.GirlFragment";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.container_global)
    CoordinatorLayout coordinator;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    private Fragment mFragment = null; //记录Activity当前持有的Fragment
    private int fc = R.id.container_content; //fragment container
    //private AlertDialog dialog; // Load Progress Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        ButterKnife.bind(this);
        StatusBarUtil.setColorForDrawerLayout(this, drawer, Color.parseColor("#373B3E"), 0);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        // 检查网络是否连接
        if (!NetStateUtils.isNetworkAvailable(this)) {
            Snackbar.make(coordinator, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
        } else {
            checkFragmentState();
            getSupportFragmentManager().beginTransaction().show(mFragment).commit();
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

            switchFragment(fm, ft, FRAGMENT_ZHIHU, fragment_zhihu, "知乎小报");

        } else if (id == R.id.drawer_guokr) {

            switchFragment(fm, ft, FRAGMENT_GUOKR, fragment_guokr, "果壳精选");

        } else if (id == R.id.drawer_douban) {

            switchFragment(fm, ft, FRAGMENT_DOUBAN, fragment_douban, "豆瓣一刻");

        } else if (id == R.id.drawer_girl) {

            switchFragment(fm, ft, FRAGMENT_GIRL, fragment_girl, "漂亮妹纸");

        } else if (id == R.id.drawer_share) {
            startActivity(new Intent(this, ShareActivity.class));
        } else if (id == R.id.drawer_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 切换 Fragment
    private void switchFragment(FragmentManager fm, FragmentTransaction ft,
                                String tag, String cname, String title) {
        if (!NetStateUtils.isNetworkAvailable(this)) {
            Snackbar.make(coordinator, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
        } else {
            checkFragmentState();

            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null) {
                ft.hide(mFragment).show(fragment).commit();
            } else {
                fragment = Fragment.instantiate(this, cname);
                ft.add(fc, fragment, tag);
                ft.hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
            toolbar.setTitle(title);
        }
    }

    // 检查 mFragment 是否已经创建
    private void checkFragmentState() {
        if (mFragment == null) {
            mFragment = Fragment.instantiate(this, "me.wondertwo.august0802.ui.fragment.ZhihuFragment");
            getSupportFragmentManager().beginTransaction().add(fc, mFragment, FRAGMENT_ZHIHU).commit();
        }
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
