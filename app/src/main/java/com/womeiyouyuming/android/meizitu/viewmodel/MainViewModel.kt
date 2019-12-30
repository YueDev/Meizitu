package com.womeiyouyuming.android.meizitu.viewmodel

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.womeiyouyuming.android.meizitu.repository.AmlyuDataSourceFactory
import com.womeiyouyuming.android.meizitu.repository.BuxiuseDataSourceFactory
import com.womeiyouyuming.android.meizitu.repository.PhotoRepository
import kotlinx.coroutines.*

/**
 * Created by Yue on 2019/12/13.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val photoRepository = PhotoRepository()

    //Amlyu相关数据
    private val amlyuDataSourceFactory = AmlyuDataSourceFactory(photoRepository)
    val amlyuPhotoListLiveData = amlyuDataSourceFactory.toLiveData(20)
    val amlyuNetworkStatusLiveData = Transformations.switchMap(amlyuDataSourceFactory.source) {
        it.networkStatus
    }

    //buxiuse
    private val buxiuseDataSourceFactory = BuxiuseDataSourceFactory(photoRepository)
    val buxiusePhotoListLiveData = buxiuseDataSourceFactory.toLiveData(20)
    val buxiuseNetworkStatusLiveData = Transformations.switchMap(buxiuseDataSourceFactory.source) {
        it.networkStatus
    }


    //刷新Amlyu数据
    fun refreshAmlyu() {
        amlyuDataSourceFactory.source.value?.invalidate()
    }

    fun refreshBuxiuse() {
        buxiuseDataSourceFactory.source.value?.invalidate()
    }


    //已在协程中处理IO线程读写并在主线程反馈结果，也可以把结果post给liveData，这样不用传lambda参数
    //分区存储，请确保android q 以前的系统获得写存储权限
    fun savePhoto(bitmap: Bitmap, photoName: String, showResult: (result: String) -> Unit) {


        viewModelScope.launch {

            val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val resolver = getApplication<Application>().contentResolver

            //图片查重
            val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
            val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(photoName)

            //图片已存在，直接执行返回，不进行存储
            //不能在协程里返回，只能从协程获取一个boolean结果，之后做判断
            val hasPhoto = withContext(Dispatchers.IO) {
                resolver.query(externalUri, projection, selection, selectionArgs, null)?.use {
                    it.count > 0
                }
            }

            if (hasPhoto == true) {
                MainScope().launch { showResult("图片已存在") }
                return@launch
            }



            //API29以上，设置IS_PENDING状态为1，这样存储结束前，其他应用就不会处理这张图片
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, photoName)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }


            //这里insertUri是nullable的，所以需要判空
            val insertUri = resolver.insert(externalUri, values) ?: let {
                showResult("出现错误，请稍后重试")
                return@launch
            }


            //IO线程开始入操作
            withContext(Dispatchers.IO) {
                resolver.openOutputStream(insertUri).use {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                        //存储结束后，android Q 需要更新IS_PENDING
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            values.clear()
                            values.put(MediaStore.Images.Media.IS_PENDING, 0)
                            resolver.update(insertUri, values, null, null)
                        }
                        MainScope().launch { showResult("图片存储成功") }

                    } else {
                        MainScope().launch { showResult("图片存储失败") }
                    }
                }
            }


        }

    }


}