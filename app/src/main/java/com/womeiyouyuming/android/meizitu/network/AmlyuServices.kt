package com.womeiyouyuming.android.meizitu.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Yue on 2019/12/18.
 */
interface AmlyuServices {

    //https://amlyu.com/page/2/


    //正常的请求，未用到
    @GET("page/{page}/")
    suspend fun getBeauty(
        @Path("page")
        page: Int
    ): Response<ResponseBody>


    //分页请求
    @GET("page/{page}/")
     fun getBeautyByPaging(
        @Path("page")
        page: Int
    ): Call<ResponseBody>
}