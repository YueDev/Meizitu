package com.womeiyouyuming.android.meizitu.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.womeiyouyuming.android.meizitu.ui.AmlyuPhotoListFragment

/**
 * Created by Yue on 2019/12/26.
 */
class PhotoListStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 1

    override fun createFragment(position: Int): Fragment {

        return AmlyuPhotoListFragment()

    }
}