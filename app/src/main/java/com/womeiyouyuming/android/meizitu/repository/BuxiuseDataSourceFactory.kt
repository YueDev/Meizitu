package com.womeiyouyuming.android.meizitu.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.womeiyouyuming.android.meizitu.model.Photo

/**
 * Created by Yue on 2019/12/27.
 */
class BuxiuseDataSourceFactory(private val photoRepository: PhotoRepository) :
    DataSource.Factory<Int, Photo>() {

    val source = MutableLiveData<BuxiuseDataSource>()

    override fun create() = BuxiuseDataSource(photoRepository).also {
        source.postValue(it)
    }

}