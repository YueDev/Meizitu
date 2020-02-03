package com.womeiyouyuming.android.meizitu.adapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.network.NetworkStatus

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
@BindingAdapter("app:bindNetworkStatusLoading")
fun bindNetworkStatusLoading(view: View, networkStatus: NetworkStatus?) {
    view.visibility = if (networkStatus == NetworkStatus.LOADING) View.VISIBLE else View.GONE
}

@BindingAdapter("app:bindNetworkStatusFailed")
fun bindNetworkStatusFailed(view: View, networkStatus: NetworkStatus?) {
    view.visibility = if (networkStatus == NetworkStatus.FAILED) View.VISIBLE else View.GONE
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