package com.womeiyouyuming.android.meizitu.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.womeiyouyuming.android.meizitu.ui.AmlyuPhotoListFragment
import com.womeiyouyuming.android.meizitu.ui.BuxiuseFragment

/**
 * Created by Yue on 2019/12/26.
 */
class PhotoListStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> AmlyuPhotoListFragment()
            1 -> BuxiuseFragment()
            else -> Fragment()
        }
    }
}