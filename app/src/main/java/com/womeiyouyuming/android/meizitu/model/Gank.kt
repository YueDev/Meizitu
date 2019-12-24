package com.womeiyouyuming.android.meizitu.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Yue on 2019/12/13.
 */



data class GankResponse(
    @SerializedName("error")
    val error: String,

    @SerializedName("results")
    val beautyList: List<BeautyImg>
)

data class BeautyImg(
    @SerializedName("url")
    val url: String
) {
    fun toPhoto() = Photo(url, url)
}






//
// gank.io/api
// 请求结果：

//{
//    "error": false,
//    "results": [
//    {
//        "_id": "5ccdbc219d212239df927a93",
//        "createdAt": "2019-05-04T16:21:53.523Z",
//        "desc": "2019-05-05",
//        "publishedAt": "2019-05-04T16:21:59.733Z",
//        "source": "web",
//        "type": "\u798f\u5229",
//        "url": "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg",
//        "used": true,
//        "who": "lijinshanmx"
//    },
//    {
//        "_id": "5cc43919fc3326376038d233",
//        "createdAt": "2019-04-27T19:12:25.536Z",
//        "desc": "2019-04-27",
//        "publishedAt": "2019-04-27T19:12:51.865Z",
//        "source": "web",
//        "type": "\u798f\u5229",
//        "url": "https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg",
//        "used": true,
//        "who": "lijinshanmx"
//    },
//    {
//        "_id": "5c6a4ae99d212226776d3256",
//        "createdAt": "2019-02-18T06:04:25.571Z",
//        "desc": "2019-02-18",
//        "publishedAt": "2019-04-10T00:00:00.0Z",
//        "source": "web",
//        "type": "\u798f\u5229",
//        "url": "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg",
//        "used": true,
//        "who": "lijinshanmx"
//    }
//    ]
//}