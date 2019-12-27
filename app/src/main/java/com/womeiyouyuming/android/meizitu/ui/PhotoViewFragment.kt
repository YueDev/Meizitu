package com.womeiyouyuming.android.meizitu.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.womeiyouyuming.android.meizitu.R
import kotlinx.android.synthetic.main.fragment_photo_view.*

/**
 * A simple [Fragment] subclass.
 */
class PhotoViewFragment : Fragment() {

    private var bitmap: Bitmap? = null

    private var isRestart = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //设置actionbar渐变
       (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_gradient, null))



        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }


    override fun onResume() {
        super.onResume()
        if (isRestart) {
            (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_gradient, null))

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //图片加载成功前禁止点击saveImage按钮
        saveImage.isEnabled = false

        val url = requireArguments().get("url").toString()
        loadPhoto(url)




        photo_view.setOnClickListener {

            //沉浸模式

            if (requireActivity().window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {

                enableFullScreen()
            } else {
                disableFullscreen()
            }

        }


        //刷新按钮
        refreshImage.setOnClickListener {
            ViewCompat.animate(it).rotation(360f).withEndAction {
                it.rotation = 0f
            }
            loadPhoto(url)
        }

        //保存图片按钮
        saveImage.setOnClickListener {
            savePhoto(url)
        }

        requireActivity().window.decorView.setOnSystemUiVisibilityChangeListener { systemUiVisibility ->
            val height = bottom_bar.height.toFloat()
            if (systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0 ) {
                ViewCompat.animate(bottom_bar).translationY(0f)
            } else {
                ViewCompat.animate(bottom_bar).translationY(height)
            }
        }

    }



    override fun onStop(){
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.colorPrimary, null))
        disableFullscreen()
        isRestart = true
    }




    //开启全屏显示(沉浸模式) 位操作

    private fun enableFullScreen() {
        requireActivity().window.decorView.systemUiVisibility =
            requireActivity().window.decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    //退出全屏显示
    private fun disableFullscreen() {
        requireActivity().window.decorView.systemUiVisibility =
            requireActivity().window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN).inv()
    }


    //保存图片应该写到viewmodel里，并且在子线程处理
    //由于glide有缓存，我就直接在主线程写了，偷懒～

    private fun savePhoto(url: String) {


        //得到uri，之后就创建Bitmap对象是利用IO流往uri里写数据，要在IO线程
        //API29以上设置IS_PENDING状态为1
        //api28以下，动态请求外部存储权限

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            try {
                val permission = ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                )

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "获取外部存储权限错误", Toast.LENGTH_SHORT).show()
            }

        }


        val imageName = url.substringAfterLast("/")
        val imageType = "image/jpeg"

        val resolver = requireActivity().applicationContext.contentResolver
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        //图片查重
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE
        )

        val selection =
            "${MediaStore.Images.Media.DISPLAY_NAME} = ? AND ${MediaStore.Images.Media.MIME_TYPE} = ?"

        val selectionArgs = arrayOf(imageName, imageType)



        try {
            resolver.query(externalUri, projection, selection,  selectionArgs, null)?.use {
                if (it.count > 0) {
                    Toast.makeText(requireContext(), "图片已存在！", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "保存失败，请检查权限", Toast.LENGTH_SHORT).show()
            return
        }


        //API29以上，设置IS_PENDING状态为1，这样其他应用就不会处理这张图片
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            put(MediaStore.Images.Media.MIME_TYPE, imageType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }







        try {

            val insertUri = resolver.insert(externalUri, values) ?: return

            resolver.openOutputStream(insertUri)?.use {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            values.clear()
            //设置IS_PENDING状态为0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(insertUri, values, null, null)
            }

            Toast.makeText(requireContext(), "图片保存成功", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "保存失败，请检查权限", Toast.LENGTH_SHORT).show()
        }

    }


    private fun loadPhoto(url: String) {


        //preload获取图片真实大小，然后加载到photo_view。
        //之后再通过glide加载图片，这样可以正常显示占位符和错误符号

        Glide.with(this).asBitmap().load(url).addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                bitmap = resource
                photo_view.setImageBitmap(bitmap)
                return false
            }
        }).preload()




        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_error_black_24dp)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(requireContext(), "加载失败，请点击右下方刷新按钮", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    saveImage.isEnabled = true
                    return false
                }
            })
            .into(photo_view)

    }

}
