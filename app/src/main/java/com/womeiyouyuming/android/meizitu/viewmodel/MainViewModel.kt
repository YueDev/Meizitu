package com.womeiyouyuming.android.meizitu.viewmodel

import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.womeiyouyuming.android.meizitu.repository.AmlyuDataSourceFactory
import com.womeiyouyuming.android.meizitu.repository.PhotoRepository

/**
 * Created by Yue on 2019/12/13.
 */
class MainViewModel: ViewModel() {

    private val photoRepository = PhotoRepository()

    //Amlyu相关数据
    private val amlyuDataSourceFactory = AmlyuDataSourceFactory(photoRepository)
    val photoListLiveData = amlyuDataSourceFactory.toLiveData(20)
    val networkStatusLiveData = Transformations.switchMap(amlyuDataSourceFactory.source) {
        it.networkStatus
    }



    //刷新Amlyu数据
    fun refreshAmlyu() {
        amlyuDataSourceFactory.source.value?.invalidate()
    }



}