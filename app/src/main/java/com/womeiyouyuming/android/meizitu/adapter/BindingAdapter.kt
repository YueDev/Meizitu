package com.womeiyouyuming.android.meizitu.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.womeiyouyuming.android.meizitu.R

/**
 * Created by Yue on 2019/12/16.
 */

@BindingAdapter("app:getImgFromURL")
fun getImgFromURL(imageView: ImageView, url: String?) {


    val glideUrl = GlideUrl(url) {
        hashMapOf("Referer" to "https://amlyu.com/")
    }


    Glide.with(imageView)
        .load(glideUrl)
        .error(R.drawable.ic_error_black_24dp)
        .into(imageView)

}