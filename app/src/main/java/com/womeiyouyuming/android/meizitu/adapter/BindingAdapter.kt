package com.womeiyouyuming.android.meizitu.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.womeiyouyuming.android.meizitu.R

/**
 * Created by Yue on 2019/12/16.
 */

@BindingAdapter("app:getImgFromAmlyu")
fun getImgFromAmlyu(imageView: ImageView, url: String?) {


    val glideUrl = GlideUrl(url) {
        hashMapOf("Referer" to "https://amlyu.com/")
    }


    Glide.with(imageView)
        .load(glideUrl)
        .error(R.drawable.ic_error_black_24dp)
        .into(imageView)
}


fun getImgFromUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_error_black_24dp)
        .into(imageView)
}