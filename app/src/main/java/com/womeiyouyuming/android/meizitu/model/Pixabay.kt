package com.womeiyouyuming.android.meizitu.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Yue on 2019/12/16.
 */


data class PixabayResponse(
    @SerializedName("totalHits")
    val totalHits: String,

    @SerializedName("hits")
    val pixabayImgList: List<PixabayImg>
)

data class PixabayImg(
    @SerializedName("largeImageURL")
    val largeImageURL: String,
    @SerializedName("webformatURL")
    val webURL: String
) {
    fun toPhoto() = Photo(webURL, largeImageURL)
}
