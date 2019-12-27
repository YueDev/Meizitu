package com.womeiyouyuming.android.meizitu.util

import com.womeiyouyuming.android.meizitu.model.Amlyu
import com.womeiyouyuming.android.meizitu.model.Buxiuse
import com.womeiyouyuming.android.meizitu.model.Photo
import org.jsoup.Jsoup


/**
 * Created by Yue on 2019/12/18
 */

//buxiuse解析
fun parseBuxiuse(result: String?) =

    result?.let {
        val elements = Jsoup.parse(it).select("div[class=thumbnail]>div[class=img_single]>a>img")
        return elements.map { e ->
            val previewUrl = e.attr("src")
            val largeUrl = previewUrl.replace("/bmiddle/", "/large/")
            Photo(previewUrl, largeUrl)
        }
    } ?: listOf<Photo>()



//amlyu解析
fun parseAmlyu(result: String?) =

    result?.let {
        val elements = Jsoup.parse(it)
            .select("div[class=excerpts]>article[class=excerpt excerpt-c5 excerpt-hoverplugin]>a>img")
        return elements.map { e ->
            Amlyu(e.attr("data-src")).toPhoto()
        }
    } ?: listOf<Photo>()
