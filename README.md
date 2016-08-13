
## About

悦乎是一个小巧精致的看知乎日报，看果壳精选，看妹纸图片的Android移动客户端APP，是我在学习 `Retorfit+RxJava` 时的练手项目，所以网络访问基于 `Retrofit+RxJava`实现。目前上线第二版 `version 1.1.0` 仍有待完善，我会在后续进行更新维护。所有内容资源均来自网络，感谢 [ izzyleung同学提供的知乎日报API分析](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90) 和 [代码家提供的干货集中营妹纸图片API](http://gank.io/api/data/%E7%A6%8F%E5%88%A9/1000/1)。可通过以下二维码下载体验，同时欢迎大家提PR！

![](http://7xt4h7.com1.z0.glb.clouddn.com/Fir.im%E5%86%85%E6%B5%8B%E5%B9%B3%E5%8F%B0-%E6%82%A6%E4%B9%8E-%E4%BA%8C%E7%BB%B4%E7%A0%81.png)


## ScreenShots

<img src="screenshots/app_yuehu_01.jpg" width="308" height="548"/>
<img src="screenshots/app_yuehu_02.jpg" width="308" height="548"/>

<img src="screenshots/app_yuehu_03.jpg" width="308" height="548"/>
<img src="screenshots/app_yuehu_04.jpg" width="308" height="548"/>


## Structure

 * Retrofit2+RxJava实现网络请求；
 * 基于WebView实现HTML网页的本地解析与加载；
 * 基于Glide框架实现的图片加载；


## Update Log

- 实现了知乎日报模块的新闻拉取列表展示；
- 实现了点击新闻列表Item进入阅读，并可以分享；
- 实现了漂亮妹纸模块的照片墙瀑布流；
- 实现了点击图片进入大图浏览页面；

下期更新内容：数据缓存；图片分享以及本地保存；优化部分代码结构；




## Open Libs
* Retrofit2
* Butterknife
* Rxjava+Rxandroid
* OkHttp
* easyrecyclerview
* Glide


## Contact

- Email: wondertwo1@163.com
- 博客：[点击这里访问我的博客](http://www.cnblogs.com/wondertwo/)


## License

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

