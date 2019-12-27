package com.womeiyouyuming.android.meizitu.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yue on 2019/12/18.
 */
interface BuxiuseServices {

    @GET("/")
    suspend fun getBeauty(
        @Query("cid")
        cid: Int,
        @Query("page")
        page: Int
    ): Response<ResponseBody>


    //查询所有图片，不用cid分类
    @GET("/")
    fun getBeautyByPaging(
        @Query("page")
        page: Int
    ): Call<ResponseBody>

}