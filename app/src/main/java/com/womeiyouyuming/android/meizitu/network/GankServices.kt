package com.womeiyouyuming.android.meizitu.network

import com.womeiyouyuming.android.meizitu.model.GankResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Yue on 2019/12/13.
 */

interface GankServices {

    @GET("random/data/福利/{number}")
    suspend fun getRandomBeauty(@Path("number") number: Int): GankResponse

}