package me.wondertwo.august0802.util;

/**
 * 网络请求常量
 *
 * Created by wondertwo on 2016/8/11.
 */
public class Constants {


    // 干货集中营 妹纸图 API，比如 http://gank.io/api/data/福利/10/1
    // 示例 http://gank.io/api/data/福利/#{number}/#{page} 参数number表示每次请求的图片个数大于0小于50
    // 参数page表示从第几页开始请求，一般page=1
    public static final String GANK_GIRL_BASE_URL = "http://gank.io/api/data/福利/";



    /**
     * 所有的知乎日报 API 的 HTTP METHOD 均为 GET
     */

    // 知乎日报base url,将文章id拼接值base url之后即可
    public static final String ZHIHU_DAILY_SHARE_URL = "http://daily.zhihu.com/story/";

    // 获取闪屏页面启动图像，start_image后面为图像分辨率
    public static final String ZHIHU_APP_START_IMAGE = "http://news-at.zhihu.com/api/4/start-images/1080*1776";

    // 知乎日报最新消息，在下面地址后面加上 latest 即可
    public static final String ZHIHU_NEWS_LATEST = "http://news-at.zhihu.com/api/4/news/";

    // 消息内容获取与离线下载
    // 在最新消息中获取到的id，拼接到这个NEWS之后，可以获得对应的JSON格式的内容
    public static final String ZHIHU_NEWS_CONTENT = "http://news-at.zhihu.com/api/4/news/";

    // 过往消息。若要查询的11月18日的消息，before后面的数字应该为20161118
    // 知乎日报创建于2013 年 5 月 19 日，如果before后面的数字小于20130520，那么返回结果为空
    public static final String ZHIHU_HISTORY_STORY = "http://news.at.zhihu.com/api/4/news/before/";

    // 新闻额外消息。输入新闻的ID，获取对应新闻的额外信息，如评论数量，所获的『赞』的数量。
    // example:http://news-at.zhihu.com/api/4/story-extra/#{id}
    public static final String ZHIHU_STORY_EXTRA_INFO = "http://news-at.zhihu.com/api/4/story-extra/";

    // 新闻对应长评论查看：使用在 最新消息 中获得的 id
    // 在 http://news-at.zhihu.com/api/4/story/#{id}/long-comments 中将 id 替换为对应的 id 得到长评论 JSON 格式的内容
    // 新闻对应短评论查看：使用在 最新消息 中获得的 id
    // 在 http://news-at.zhihu.com/api/4/story/#{id}/short-comments 中将 id 替换为对应的 id 得到短评论 JSON 格式的内容
    public static final String ZHIHU_STORY_COMMENTS = "http://news-at.zhihu.com/api/4/story/";




    // 主题日报列表查看
    public static final String ZHIHU_THEMES_LIST = "http://news-at.zhihu.com/api/4/themes";

    // 主题日报内容查看：使用在 主题日报列表查看 中获得需要查看的主题日报的 id
    // 拼接在 http://news-at.zhihu.com/api/4/theme/ 后得到对应主题日报 JSON 格式的内容
    public static final String ZHIHU_THEME_CONTENT = "http://news-at.zhihu.com/api/4/theme/";


    // 热门消息。请注意！此 API 仍可访问，但是其内容未出现在最新的『知乎日报』 App 中。
    public static final String ZHIHU_HOT_LIST = "http://news-at.zhihu.com/api/3/news/hot";

    // 查看热门消息的推荐者 ："http://news-at.zhihu.com/api/4/story/#{id}/recommenders" 将新闻id填入到#{id}的位置
    public static final String ZHIHU_HOT_RECOMMENDERS = "http://news-at.zhihu.com/api/3/news/hot";

    // 获取某个专栏之前的新闻
    // http://news-at.zhihu.com/api/4/theme/#{theme id}/before/#{id}
    // 将专栏id填入到 #{theme id}, 将新闻id填入到#{id}
    // 如 http://news-at.zhihu.com/api/4/theme/11/before/7119483
    // 注：新闻id要是属于该专栏，否则，返回结果为空

    // 查看editor的主页
    // http://news-at.zhihu.com/api/4/editor/#{id}/profile-page/android



    // 果壳精选 base url
    public static final String GUOKR_NEWS_BASE_URL = "http://apis.guokr.com/";

    // 获取果壳精选的文章列表,通过组合相应的参数成为完整的url
    // public static final String GUOKR_ARTICLES = "http://apis.guokr.com/handpick/article.json?retrieve_type=by_since&category=all&limit=20&ad=1";

    // 获取果壳文章的具体信息 V1
    public static final String GUOKR_ARTICLE_LINK_V1 = "http://jingxuan.guokr.com/pick/";

    // 获取果壳文章的具体信息 V2
    public static final String GUOKR_ARTICLE_LINK_V2 = "http://jingxuan.guokr.com/pick/v2/";

    // 获取果壳精选的轮播文章列表
    // public static final String GUOKR_HANDPICK_CAROUSEL = "http://apis.guokr.com/flowingboard/item/handpick_carousel.json";


    // 豆瓣一刻
    // 根据日期查询消息列表
    // eg:https://moment.douban.com/api/stream/date/2016-08-11
    public static final String DOUBAN_MOMENT = "https://moment.douban.com/api/stream/date/";

    // 获取文章具体内容
    // eg:https://moment.douban.com/api/post/100484
    public static final String DOUBAN_ARTICLE = "https://moment.douban.com/api/post/";
}
