package com.womeiyouyuming.android.meizitu.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Yue on 2019/12/13.
 */
object RetrofitClient {

    //https://pixabay.com/api/?key=14638732-461a2812cd8e3aae86ff423d1&q=asian+girl&image_type=photo&per_page=50&page=1


    //    //https://amlyu.com/page/2/

    private const val GANK_BASE_URL = "http://gank.io/api/"
    private const val PIXABAY_BASE_URL = "https://pixabay.com/"
    private const val BUXIUSE_BASE_URL = "https://www.buxiuse.com/"
    private const val AMLYU_BASE_URL = "https://amlyu.com/"







    private val gankRetrofit =
        Retrofit.Builder().baseUrl(GANK_BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val pixabayRetrofit =
        Retrofit.Builder().baseUrl(PIXABAY_BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val buxiuseRetrofit = Retrofit.Builder().baseUrl(BUXIUSE_BASE_URL).build()

    private val amlyuRetrofit = Retrofit.Builder().baseUrl(AMLYU_BASE_URL).build()

    fun getGankServices(): GankServices = gankRetrofit.create(GankServices::class.java)

    fun getPixabayServices(): PixabayServices = pixabayRetrofit.create(PixabayServices::class.java)

    fun getBuxiuseServices(): BuxiuseServices = buxiuseRetrofit.create(BuxiuseServices::class.java)

    fun getAmlyuServices(): AmlyuServices = amlyuRetrofit.create(AmlyuServices::class.java)



}