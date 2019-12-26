package com.womeiyouyuming.android.meizitu.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.adapter.PhotoListAdapter
import com.womeiyouyuming.android.meizitu.network.NetworkStatus
import com.womeiyouyuming.android.meizitu.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_amlyu_photo_list.*



class BuxiuseFragment : Fragment() {


    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buxiuse, container, false)
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

        //        弹性布局 瀑布流
        val photoLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
        }


        val photoAdapter = PhotoListAdapter {
            val bundle = bundleOf("url" to it)
            findNavController().navigate(R.id.action_viewPagerFragment_to_photoViewFragment, bundle)
        }


        photo_recycler_view.apply {
            layoutManager = photoLayoutManager
            adapter = photoAdapter
        }

        mainViewModel.photoListLiveData.observe(viewLifecycleOwner, Observer {
            photoAdapter.submitList(it)
        })
    }


    private fun initRefreshLayout() {




        mainViewModel.networkStatusLiveData.observe(viewLifecycleOwner, Observer {

            //下拉刷新
            swipe_refresh.isRefreshing = it == NetworkStatus.LOADING
            swipe_refresh.setOnRefreshListener {
                mainViewModel.refreshAmlyu()
            }

            //错误提示
            if (it == NetworkStatus.FAILED) {
                Toast.makeText(requireContext(), "出现错误，请检查网络后下拉或者点右上角刷新", Toast.LENGTH_SHORT).show()
            }

        })
    }


}
