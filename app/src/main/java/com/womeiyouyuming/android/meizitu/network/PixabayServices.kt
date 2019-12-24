package com.womeiyouyuming.android.meizitu.network

import com.womeiyouyuming.android.meizitu.model.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yue on 2019/12/16.
 */
interface PixabayServices {


    //https://pixabay.com/api/?key=14638732-461a2812cd8e3aae86ff423d1&q=asian+girl&image_type=photo&per_page=50&page=1
    @GET("api/")
    suspend fun getImage(
        @Query("key")
        key: String,
        @Query("q")
        q: String,
        @Query("image_type")
        imageType: String,
        @Query("per_page")
        perPage: String,
        @Query("page")
        page: String
    ): PixabayResponse



}