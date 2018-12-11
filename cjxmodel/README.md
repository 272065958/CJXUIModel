[![](https://www.jitpack.io/v/com.gitee.cjx_code/cjxmodel.svg)](https://www.jitpack.io/#com.gitee.cjx_code/cjxmodel)
### cjxModel
cjxModel是使用Kotlin语言编写的一套Android基础框架,主要实现单Activity+多Fragment的App开发

### 在项目build.gradle上添加


```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

    

### 在app的build.gradle上添加

```
dependencies {
    implementation 'com.gitee.cjx_code:cjxmodel:v1.0-beta'
}
```


### 模块功能

- 程序入口类 
1. **[MyApplication](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/MyApplication.kt)** 
实现获取当前屏幕宽高,界面主题样式和用户数据相关的抽象方法

- UI基类包,你可以重写大部分方法达到自定义的效果
1. **[BaseActivity](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/activity/BaseActivity.kt)** 实现一些基础的界面方法,包括设置标题栏,显示提示消息,显示/隐藏加载框,显示/隐藏软键盘,注册和自动注销广播监听,fragment的管理
2. **[BaseMainTabActivity](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/activity/BaseMainTabActivity.kt)** 实现一个包含badge的底部Tab的基类,子类最少只需要实现两个函数就能完成一个包含分类的首页
3. **[BaseFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseFragment.kt)** 实现一个类似[BaseActivity](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/activity/BaseActivity.kt)功能的Fragment基类
4. **[BaseBottomTabFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseBottomTabFragment.kt)** 实现一个类似BaseMainTabActivity功能的Fragment基类
5. **[BaseLoadFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseLoadFragment.kt)** 基于BaseFragment实现一个加载数据并显示的Fragment,可以通过在初始化界面设置openRefresh来设置是否有下拉刷新功能,该已集成加载数据的回调接口,子类只需要实现LoadData(加载网络数据)和displayData(获取网络数据成功后的回调)的回调和createContentView(创建一个显示网络数据的contentView)的方法, 通过加载数据的情况决定显示LoadView或者EmptyView或者显示加载内容的contentView
6. **[BaseRecyclerFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseRecyclerFragment.kt)** 基于[BaseLoadFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseLoadFragment.kt)实现一个默认加载列表数据并显示的Fragment,子类最少只需要多实现获取Adapter的接口,就可以完成一个列表界面,可以在初始化界面前设置openLoadMore来控制是否允许加载下一页数据
7. **[BaseTabLayoutPagerFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseTabLayoutPagerFragment.kt)** 基于[BaseFragment](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/fragment/BaseFragment.kt)实现的顶部Tab翻页基类, 最少只需要实现获取顶部tab标题数组和每个标题对应的Fragment方法就可以完成界面
8. **[BaseRecyclerAdapter](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/adapter/BaseRecyclerAdapter.kt)** RecyclerView的基类适配器,最少只需要实现获取Item的View和绑定Item的方法
9. **[BaseRecyclerWrapperAdapter](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/adapter/BaseRecyclerWrapperAdapter.kt)** 基于[BaseRecyclerAdapter](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/base/adapter/BaseRecyclerAdapter.kt)实现可以添加HeaderView和FooterView的适配器

- 工具包
1. **[ImageLoadUtil](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/common/ImageLoadUtil.kt)** 用Glide的插件实现加载的图片的工具
2. **[JsonParser](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/common/JsonParser.kt)** 用Gson插件实现json的解析和转化的工具
3. **[MyLog](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/common/MyLog.kt)** 只有在debug模式下才会打印log的工具
4. **[Tools](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/common/Tools.kt)** App的通用工具类,包含创建缓存文件夹,字符串格式的判断的

- 界面组件包
1. **[AdverPlayView](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/AdverPlayView.kt)** 常见的轮播广告,有自动播放和无限循环功效
2. **[FooterLoadRecyclerView](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/FooterLoadRecyclerView.kt)** 包含加载下一页功效的RecyclerView
3. **[MyLoadView](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/MyLoadView.kt)** 适配了Android5.0之后的加载进度View
4. **[PagePointView](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/PagePointView.kt)** 显示页码的View
5. **[RecyclerViewGridDivider](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/RecyclerViewGridDivider.kt)** RecyclerView显示表格样式时的分隔线
6. **[RecyclerViewLinearDivider](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/component/RecyclerViewLinearDivider.kt)** RecyclerView显示线性样式时的分隔线

- 网络工具包
1. **[HttpCallbackInterface](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/HttpCallbackInterface.kt)** 网络加载回调接口
2. **[HttpUtils](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/HttpUtils.kt)** 访问网络的工具,需要在程序入口设置Api的域名,postEnqueue请求Api接口,可以用setFormContentType来设置ContentType,也包含下载和上传的方法
3. **[ProgressRequestBody](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/ProgressRequestBody.kt)** 请求数据的响应类,当要实现上传进度的时候使用
4. **[ProgressResponseBody](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/ProgressResponseBody.kt)** 响应数据的响应类,当腰实现下载进度的时候使用
5. **[ProgressRequestHandler](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/ProgressRequestHandler.kt)** 上传进度监听
6. **[DownloadUtil](https://gitee.com/cjx_code/cjxmodel/blob/master/src/main/java/com/model/cjx/http/DownloadUtil.kt)** 下载工具类