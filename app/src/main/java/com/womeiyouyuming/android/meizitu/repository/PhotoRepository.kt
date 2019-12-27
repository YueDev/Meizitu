package com.womeiyouyuming.android.meizitu.repository

import com.womeiyouyuming.android.meizitu.model.Photo
import com.womeiyouyuming.android.meizitu.network.RetrofitClient
import com.womeiyouyuming.android.meizitu.util.parseBuxiuse


/**
 * Created by Yue on 2019/12/13.
 */
class PhotoRepository {



    private val amlyuServices = RetrofitClient.getAmlyuServices()


    fun getPhotosFromAmlyuPaging(page: Int) = amlyuServices.getBeautyByPaging(page)

    fun getPhotosFromBuxiusePading(page: Int) = buxiuseServices.getBeautyByPaging(page)




    //下边都是加载其他网站的，未用到


    private val gankServices = RetrofitClient.getGankServices()
    private val pixabayServices = RetrofitClient.getPixabayServices()
    private val buxiuseServices = RetrofitClient.getBuxiuseServices()
    private val pixabayKey = "14638732-461a2812cd8e3aae86ff423d1"
    private val pixabayQ = "asian+girl"
    private val pixabayImageType = "photo"
    private val pixabayPerPage = "50"
    private val pixabayPage = (1..8).random().toString()


    suspend fun getPhotosFromGank(number: Int) = try {
        gankServices.getRandomBeauty(number).beautyList.map {
            it.toPhoto()
        }
    } catch (e: Exception) {
        listOf<Photo>().also {
            e.printStackTrace()
        }
    }

    suspend fun getPhotosFromPixabay() = try {
        pixabayServices.getImage(
            pixabayKey,
            pixabayQ,
            pixabayImageType,
            pixabayPerPage,
            pixabayPage
        ).pixabayImgList.map {
            it.toPhoto()
        }


    } catch (e: Exception) {
        listOf<Photo>().also {
            e.printStackTrace()
        }
    }

    suspend fun getPhotosFromBuxiuse() = try {
        val result = buxiuseServices.getBeauty(6, 2).body()?.string()
        parseBuxiuse(result)
    } catch (e: Exception) {
        listOf<Photo>().also {
            e.printStackTrace()
        }
    }


}



