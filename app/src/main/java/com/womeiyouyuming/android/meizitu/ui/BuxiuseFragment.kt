package com.womeiyouyuming.android.meizitu.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.adapter.BuxiuseListAdapter
import com.womeiyouyuming.android.meizitu.network.NetworkStatus
import com.womeiyouyuming.android.meizitu.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_amlyu_photo_list.*


class BuxiuseFragment : Fragment() {


    private val mainViewModel by activityViewModels<MainViewModel>()


    //
    //随机item的layoutWidth，造成瀑布流的效果
    //这里预先存储200个随机数，防止复用时布局宽度改变
    //由于ListAdapter的数据长度未知，所以给定一个100的长度，使用的时候取余即可。

    //fragment重建会，可以放到viewmodel里或者用object
    //这里我放到onAttach()里，这样fragment从photoview页面退回来的时候不会执行onAttach，布局不会变
    //但是旋转的时候会执行onAttach，布局改变，想要的就是这个效果

    //后来发现懒加载更方便。。。我就放到这里了
    private val itemWithList: List<Int> by lazy {
        List(200) {
            (300..600).random()
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_buxiuse, container, false)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("YUEDEV", "buxiuse viewRestored")
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
                mainViewModel.refresBuxiuse()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    private fun initRecyclerView() {

        //        弹性布局 瀑布流
        val photoLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
        }


        val photoAdapter = BuxiuseListAdapter(itemWithList) {
            val bundle = bundleOf("url" to it)
            findNavController().navigate(R.id.action_viewPagerFragment_to_photoViewFragment, bundle)
        }


        photo_recycler_view.apply {
            layoutManager = photoLayoutManager
            adapter = photoAdapter
        }

        mainViewModel.buxiusePhotoListLiveData.observe(viewLifecycleOwner, Observer {
            photoAdapter.submitList(it)
        })
    }


    private fun initRefreshLayout() {


        mainViewModel.buxiuseNetworkStatusLiveData.observe(viewLifecycleOwner, Observer {

            //下拉刷新
            swipe_refresh.isRefreshing = it == NetworkStatus.LOADING
            swipe_refresh.setOnRefreshListener {
                mainViewModel.refresBuxiuse()
            }

            //错误提示
            if (it == NetworkStatus.FAILED) {
                Toast.makeText(requireContext(), "出现错误，请检查网络后下拉或者点右上角刷新", Toast.LENGTH_SHORT).show()
            }

        })
    }


}
