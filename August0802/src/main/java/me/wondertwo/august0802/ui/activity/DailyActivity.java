package me.wondertwo.august0802.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wondertwo.august0802.R;
import me.wondertwo.august0802.bean.RetrofitClient;
import me.wondertwo.august0802.bean.zhihu.DailyArticle;
import me.wondertwo.august0802.util.Constants;
import me.wondertwo.august0802.util.NetStateUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wondertwo on 2016/8/10.
 */
public class DailyActivity extends BaseActivity {

    private String TAG = "DailyActivity";

    protected Subscription mSubscription;

    private int storyId; // 文章ID
    private String storyTitle; // 文章标题
    private String shareUrl; // 分享链接
    // private String storyUrl; // 文章链接

    // passage comments fields
    // private int like_count = 0; //likes
    // private int comment_count = 0; //comments

    @Bind(R.id.activity_article_co)
    CoordinatorLayout mCoorLayout;
    @Bind(R.id.article_web_view)
    WebView mWebView;
    @Bind(R.id.article_body_title)
    TextView mBodyTitle;
    @Bind(R.id.article_float_btn)
    FloatingActionButton mContentFloatBtn;
    @Bind(R.id.article_tool_btn)
    Toolbar mContentToolbar;
    @Bind(R.id.article_header_iv)
    ImageView mHeaderIv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mContentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 获取传递过来的id/title/image
        Intent intent = getIntent();
        storyId = intent.getIntExtra("story_id", 0);
        shareUrl = Constants.ZHIHU_DAILY_SHARE_URL + storyId;
        storyTitle = intent.getStringExtra("story_title");
        // 设置title
        mBodyTitle.setText(storyTitle);
        mHeaderIv.setImageResource(R.drawable.content_no_image);

        // 配置 WebView
        configWebViewAttrs();
        // 检查网络，执行网络请求
        if (!NetStateUtils.isNetworkAvailable(this)) {
            Snackbar.make(mCoorLayout, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
        } else {
            executeRequestData(storyId);
        }

        // 分享按钮事件
        configFloatingBtn();

    }

    // 设置 WebView 相关属性
    private void configWebViewAttrs() {
        //设置 WebView 属性
        mWebView.setScrollbarFadingEnabled(true);

        //能够和js交互
        mWebView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        mWebView.getSettings().setBuiltInZoomControls(false);
        //缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        mWebView.getSettings().setAppCacheEnabled(false);


        //设置不调用第三方浏览器即可进行页面反应
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

        });

        //设置在本WebView内可以通过按下返回上一个html页面
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    // 执行网络请求
    private void executeRequestData(int contentId) {
        unSubscribe();

        mSubscription = RetrofitClient.getDailyArticleService()
                .getDailyArticles(contentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyArticle>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "--------completed-------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "--------error-------");
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(DailyArticle dailyArticle) {

                        /*if (dailyArticle.share_url != null) {
                            shareUrl = dailyArticle.share_url;
                        } else {
                            shareUrl = "SORRY，分享链接为空";
                        }*/

                        // 设置页面顶部图片
                        Glide.with(DailyActivity.this).load(dailyArticle.image)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.drawable.content_no_image)
                                .into(mHeaderIv);

                        /**
                         * 此处坑多，注意怎样组装好 HTML
                         */
                        if (dailyArticle.body != null) {

                            // HTML头部，其中 <link href="file:///android_asset/zhihu_master.css" type="text/css" rel="stylesheet"/>
                            // 指向CSS文件所在目录，这里加载应用本地CSS文件，CSS文件已提前下载好存放在在项目的 /main/assets/目录下
                            String html_header = "<html><head><link href=\"file:///android_asset/zhihu_master.css\" type=\"text/css\" rel=\"stylesheet\"/></head><body>";
                            // HTML尾部
                            String html_footer = "</body></html>";

                            // body中替换掉img-place-holder div 可以去除网页中div所占的区域
                            // 否则整个网页的头部将会出现一部分的空白区域
                            String body = dailyArticle.body.replace("<div class=\"img-place-holder\">", "")
                                    .replace("<div class=\"headline\">", "");

                            // 组装成一个完整的 HTML 页面
                            String html = html_header + body + html_footer;

                            // WebView 加载 HTML 页面
                            mWebView.loadDataWithBaseURL("file:///android_assets", html, "text/html", "UTF-8", null);

                        } else {

                            // 若 body 为空，share_url一般不为空，从share_url加载页面
                            mWebView.loadUrl(dailyArticle.share_url);
                            mHeaderIv.setImageResource(R.drawable.content_no_image);
                            mHeaderIv.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        }
                    }
                });
    }



    // 悬浮按钮点击事件，文章分享
    private void configFloatingBtn() {
        mContentFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                    String shareText = storyTitle + " " + shareUrl + " " + getString(R.string.share_message);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                    startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));
                } catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(mContentFloatBtn , R.string.wrong_process, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_toolbar_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // 页面左上角返回按钮事件监听
            finish();
            return true;
        } else if (id == R.id.action_comments) {

            // 获取文章评论情况
            // show a dialog, to show the comments
            return true;

        } else if (id == R.id.action_open_in_browser) {
            // 在浏览器中打开
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(shareUrl)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    protected void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
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
