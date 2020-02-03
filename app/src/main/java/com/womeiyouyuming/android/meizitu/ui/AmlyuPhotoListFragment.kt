package com.womeiyouyuming.android.meizitu.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.adapter.PhotoListAdapter
import com.womeiyouyuming.android.meizitu.network.NetworkStatus
import com.womeiyouyuming.android.meizitu.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_amlyu_photo_list.*
import kotlinx.android.synthetic.main.fragment_view_pager.*


/**
 * A simple [Fragment] subclass.
 */
class AmlyuPhotoListFragment : Fragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_amlyu_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        activity为了响应沉浸模式而设置了全屏显示，所以用不到沉浸模式的fragment就需要整体向下移动，给layout设置内部上边距
//        获取状态栏高度，layout内部上边距 = actionbar高度 + 状态栏高度
//        这一块放到上一层布局viewpager中了


//        val id = resources.getIdentifier("status_bar_height", "dimen","android")
//        val statusBarSize = resources.getDimension(id)
//        val padding = statusBarSize.toInt() + layout.paddingTop
//
//        layout.setPadding(0, padding, 0, 0)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //图片列表
        initRecyclerView()

        //下拉刷新
        initRefreshLayout()
    }


    //右上角菜单
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.item_refresh -> {
                mainViewModel.refreshAmlyu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    private fun initRecyclerView() {

        val gridLayoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)


        val photoAdapter = PhotoListAdapter(itemClick = {
            val bundle = bundleOf("url" to it)
            findNavController().navigate(R.id.action_viewPagerFragment_to_photoViewFragment, bundle)
        }, retry = {
            mainViewModel.retryAmlyu()
        })


        photo_recycler_view.apply {
            layoutManager = gridLayoutManager
            adapter = photoAdapter
        }

        mainViewModel.amlyuPhotoListLiveData.observe(viewLifecycleOwner, Observer {
            photoAdapter.submitList(it)
        })

        mainViewModel.amlyuNetworkStatusLiveData.observe(viewLifecycleOwner, Observer {
            photoAdapter.setNetworkStatus(it)
        })
    }


    private fun initRefreshLayout() {

//        弹性布局 瀑布流
//        val photoLayoutManager = FlexboxLayoutManager(requireContext()).apply {
//            flexWrap = FlexWrap.WRAP
//        }


        mainViewModel.amlyuNetworkStatusLiveData.observe(viewLifecycleOwner, Observer {


            //下拉刷新
            swipe_refresh.isRefreshing = it == NetworkStatus.FIRST_LOADING
            swipe_refresh.setOnRefreshListener {
                mainViewModel.refreshAmlyu()
            }


        })
    }


}




