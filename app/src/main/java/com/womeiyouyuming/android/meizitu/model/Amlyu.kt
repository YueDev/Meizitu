package com.womeiyouyuming.android.meizitu.model

/**
 * Created by Yue on 2019/12/18.
 */


data class Amlyu(
    val url:String
) {
    fun toPhoto() = Photo(url, url)
}