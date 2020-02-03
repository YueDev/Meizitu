package com.womeiyouyuming.android.meizitu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.databinding.ItemNetworkStatusBinding
import com.womeiyouyuming.android.meizitu.databinding.ItemPhotoBinding
import com.womeiyouyuming.android.meizitu.model.Photo
import com.womeiyouyuming.android.meizitu.network.NetworkStatus


/**
 * Created by Yue on 2019/12/16.
 */


class PhotoListAdapter(
    private val itemClick: (url: String) -> Unit,
    private val retry: () -> Unit
) :
    PagedListAdapter<Photo, RecyclerView.ViewHolder>(PhotoCallback) {


    private var networkStatus: NetworkStatus? = null

    fun setNetworkStatus(networkStatus: NetworkStatus?) {
        val hadExtraRow = hasExtraRow()
        this.networkStatus = networkStatus
        val hasExtraRow = hasExtraRow()

        when {
            hadExtraRow && !hasExtraRow -> notifyItemRemoved(super.getItemCount())
            !hadExtraRow && hasExtraRow -> notifyItemInserted(super.getItemCount())
            // 这里如果没有数据刷新的话会出现越界错误 notifyItemChanged（-1），所以再加一个判断
            hasExtraRow -> notifyItemChanged(itemCount - 1)
        }

    }

    private fun hasExtraRow() =
        (networkStatus == NetworkStatus.FAILED) || (networkStatus == NetworkStatus.LOADING)


    override fun  onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.item_photo -> PhotoHolder(DataBindingUtil.inflate(inflater, R.layout.item_photo, parent, false))
            R.layout.item_network_status -> NetworkStatusHolder(DataBindingUtil.inflate(inflater, R.layout.item_network_status, parent, false), retry).also {
                (it.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            }
            else -> throw IllegalArgumentException("No such viewType: $viewType")
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            R.layout.item_photo -> (holder as PhotoHolder).bind(getItem(position), itemClick)
            R.layout.item_network_status -> (holder as NetworkStatusHolder).bind(networkStatus)
        }

    }



    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) R.layout.item_network_status else R.layout.item_photo


    object PhotoCallback : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem

    }


    class PhotoHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo?, itemClick: (url: String) -> Unit) {

            photo ?: return


            getImgFromAmlyu(binding.itemImageView, photo.previewUrl)

            binding.root.setOnClickListener {
                itemClick(photo.largeUrl)
            }


        }
    }


    class NetworkStatusHolder(private val binding: ItemNetworkStatusBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry()
            }
        }

        fun bind(networkStatus: NetworkStatus?) {

            networkStatus?.let {
                binding.networkStatus = it
            }

        }

    }

}
