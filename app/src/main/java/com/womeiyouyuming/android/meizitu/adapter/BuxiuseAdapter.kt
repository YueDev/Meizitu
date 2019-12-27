package com.womeiyouyuming.android.meizitu.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.womeiyouyuming.android.meizitu.R

import com.womeiyouyuming.android.meizitu.model.Photo

/**
 * Created by Yue on 2019/12/27.
 */
class BuxiuseListAdapter(
    private val itemWidthList: List<Int>,
    private val itemClick: (url: String) -> Unit
) :
    PagedListAdapter<Photo, BuxiuseListAdapter.PhotoHolder>(PhotoListAdapter.PhotoCallback) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoHolder {
        val inflater = LayoutInflater.from(parent.context)

        val itemView = inflater.inflate(R.layout.item_photo, parent, false)
        return PhotoHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        holder.bind(getItem(position), itemWidthList[position % itemWidthList.size], itemClick)
    }


    class PhotoHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.item_image_view)

        fun bind(photo: Photo?, layoutWidth: Int, itemClick: (url: String) -> Unit) {

            photo ?: return


            //设定弹性布局的宽度以及flexGrow，flexGrow会让布局填充每一行的剩余的空白，数值类似weight的占比

            val layoutParams = itemView.layoutParams as FlexboxLayoutManager.LayoutParams

            layoutParams.width = layoutWidth
            layoutParams.flexGrow = 1f


            getImgFromUrl(imageView, photo.previewUrl)

            imageView.setOnClickListener {
                itemClick(photo.largeUrl)
            }

        }
    }

}