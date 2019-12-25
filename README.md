# 妹子图

## 简介

一个简单的看妹子的app，主要练习paging网络分页加载。

内置多个数据源，但是还没有用到。以后会慢慢使用更多数据源。

## 预览

<img src="./img/Screenshot_2019-12-24-10-56-53-16_39ca5382d959fcc1e205bdb869aa86aa.jpg" height="400">

<img src="./img/Screenshot_2019-12-24-10-57-02-98_39ca5382d959fcc1e205bdb869aa86aa.jpg" height="400">


## 一些问题

1. 沉浸模式 

为了响应沉浸模式，整个Activity设置了全屏显示

~~~
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

~~~

这样就需要在非沉浸的fragment里做布局的向下偏移

~~~
        //activity为了响应沉浸模式而设置了全屏显示，所以用不到沉浸模式的fragment就需要整体向下移动，给layout设置内部上边距
        //获取状态栏高度，layout内部上边距 = actionbar高度 + 状态栏高度

        val id = resources.getIdentifier("status_bar_height", "dimen","android")
        val statusBarSize = resources.getDimension(id)
        val padding = statusBarSize.toInt() + layout.paddingTop

        layout.setPadding(0, padding, 0, 0)
~~~

这样做很繁琐，如果单独给沉浸模式的内容放到一个Activity里会简单很多。

2. actionbar的透明渐变效果，类似Google的看图界面，还没有做。


