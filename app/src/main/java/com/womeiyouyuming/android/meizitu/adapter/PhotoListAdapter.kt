package com.womeiyouyuming.android.meizitu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.databinding.ItemPhotoBinding
import com.womeiyouyuming.android.meizitu.model.Photo


/**
 * Created by Yue on 2019/12/16.
 */


class PhotoListAdapter(private val itemClick:(url:String) -> Unit) : PagedListAdapter<Photo, PhotoListAdapter.PhotoHolder>(PhotoCallback) {

    //随机item的layoutWidth，造成瀑布流的效果
    //这里预先存储50个随机数，防止复用时布局宽度改变
    //由于ListAdapter的数据长度未知，所以给定一个50的长度，使用的时候取余即可。
    private val layoutWidthList = List(50) {
        (300..600).random()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPhotoBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_photo, parent, false)
        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        holder.bind(getItem(position), layoutWidthList[position % layoutWidthList.size], itemClick)

    }



    object PhotoCallback : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem

    }

    class PhotoHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo?, layoutWidth: Int, itemClick:(url:String) -> Unit) {

            photo ?: return

            val layoutParams = itemView.layoutParams

            //设定弹性布局的宽度以及flexGrow，flexGrow会让布局填充每一行的剩余的空白，数值类似weight的占比
            if (layoutParams is FlexboxLayoutManager.LayoutParams) {
                layoutParams.width = layoutWidth
                layoutParams.flexGrow = 1f
            }

            getImgFromURL(binding.itemImageView, photo.previewUrl)

            binding.root.setOnClickListener {
                itemClick(photo.largeUrl)
            }


        }
    }
}
