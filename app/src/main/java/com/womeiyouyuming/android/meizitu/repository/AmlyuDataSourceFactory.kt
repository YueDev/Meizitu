package com.womeiyouyuming.android.meizitu.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.womeiyouyuming.android.meizitu.model.Photo

/**
 * Created by Yue on 2019/12/19.
 */


//AmlyuDataSource不能直接调用，需要通过工厂模式
class AmlyuDataSourceFactory(private val photoRepository: PhotoRepository) :
    DataSource.Factory<Int, Photo>() {

    val source = MutableLiveData<AmlyuDataSource>()

    override fun create() = AmlyuDataSource(photoRepository).also {
        source.postValue(it)
    }
}