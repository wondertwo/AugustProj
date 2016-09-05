
## About

悦乎是一个小巧精致的综合阅读类Android客户端App，目前已经更新到version1.8.0版本，集成的功能有知乎小报（数据来源：[知乎日报](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)），
果壳精选（数据来源：果壳网），豆瓣一刻（数据来源：豆瓣网），漂亮妹纸（数据来源：[干货集中营福利](http://gank.io/api/data/%E7%A6%8F%E5%88%A9/1000/1)），感谢数据内容的提供者。
可扫描以下二维码下载体验，欢迎大家star！


## Screenshots

<img src="screenshots/app_yuehu_01.jpg" width="308" height="548"/>
<img src="screenshots/app_yuehu_02.jpg" width="308" height="548"/>

<img src="screenshots/app_yuehu_03.jpg" width="308" height="548"/>
<img src="screenshots/app_yuehu_04.jpg" width="308" height="548"/>


## Structure

* 基于Retrofit2+RxJava实现网络请求；
* 基于WebView实现HTML网页的本地解析与加载；
* 基于Glide框架实现的图片加载；


## Update Log

> 下期更新内容：图片本地保存、编辑；收藏夹功能；优化部分代码结构；

- build 6, version 1.8.0

    1. 新增   所有Activity页面实现透明状态栏（即沉浸式状态栏）
    2. 新增   判断当前网络状况，提示用户当前网络状况
    3. 新增   关于应用、快乐分享页面
    4. 新增   知乎小报、豆瓣一刻列表页日期选择器，按日加载当天数据
    5. 修复   重复退出/进入应用时，Fragment重复加载叠加问题
    6. 修复   网络状况不佳等极端情况下CRASH
    7. 修复   更换部分icon以及图片，调整了部分menu菜单项
    8. 修复   优化项目60%以上的代码结构

- build 4, version 1.4.0

    1. 新增   果壳精选模块的网络数据内容加载，列表展示
    2. 新增   果壳精选文章浏览页HTML本地解析加载
    3. 新增   豆瓣一刻模块数据加载
    4. 新增   豆瓣一刻模块文章浏览页HTML数据本地解析加载
    5. 新增   知乎小报，果壳精选，豆瓣一刻图文浏览页悬浮按钮分享到微信、QQ等应用
    6. 新增   知乎小报，果壳精选，豆瓣一刻图文浏览页在手机浏览器打开浏览
    7. 修复   进入漂亮妹纸、果壳精选模块隐藏悬浮按钮
    7. 修复   优化部分代码结构

- build 2, 3, version 1.2.0

    1. 新增   知乎小报模块的从网络拉取数据
    2. 新增   知乎小报模块点击列表项阅读文章内容功能
    3. 新增   漂亮妹纸模块从网络拉取图片，瀑布流
    4. 新增   点击妹纸图片进入大图显示页面
    5. 修复   文章浏览页面顶部大图预加载
    6. 修复   下拉刷新出现卡顿的问题


## Open Libs

* Retrofit2
* Butterknife
* RxJava+RxAndroid
* OkHttp
* easyrecyclerview
* Glide


## Contact

- Email: wondertwo1@163.com
- 博客：[点击这里访问我的博客](http://www.cnblogs.com/wondertwo/)


## License

```
Copyright 2016 http://www.wondertwo.me

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

