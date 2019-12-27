package com.womeiyouyuming.android.meizitu.adapter

import android.view.LayoutInflater
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


class PhotoListAdapter(private val itemClick: (url: String) -> Unit) :
    PagedListAdapter<Photo, PhotoListAdapter.PhotoHolder>(PhotoCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPhotoBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_photo, parent, false)
        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        holder.bind(getItem(position), itemClick)

    }


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
}
