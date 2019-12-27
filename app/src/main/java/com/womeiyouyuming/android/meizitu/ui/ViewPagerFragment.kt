package com.womeiyouyuming.android.meizitu.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.adapter.PhotoListStateAdapter
import kotlinx.android.synthetic.main.fragment_view_pager.*

/**
 * A simple [Fragment] subclass.
 */
class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //activity为了响应沉浸模式而设置了全屏显示，所以用不到沉浸模式的fragment就需要整体向下移动，给layout设置内部上边距
        //获取状态栏高度，layout内部上边距 = actionbar高度 + 状态栏高度

        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarSize = resources.getDimension(id)
        val padding = statusBarSize.toInt() + layout.paddingTop
        layout.setPadding(0, padding, 0, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = PhotoListStateAdapter(this)
        view_pager.adapter = adapter

        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = when (position) {
                0 -> "喵领域"
                1 -> "不羞涩"
                else -> ""
            }
        }.attach()
    }

}
