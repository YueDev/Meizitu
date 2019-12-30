package com.womeiyouyuming.android.meizitu.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.womeiyouyuming.android.meizitu.R
import com.womeiyouyuming.android.meizitu.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_photo_view.*

/**
 * A simple [Fragment] subclass.
 */
class PhotoViewFragment : Fragment() {

    private val requestCodeForWriteExternalStorage = 1

    private var bitmap: Bitmap? = null
    private var isRestart = false

    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //设置actionbar渐变
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            resources.getDrawable(
                R.drawable.action_bar_gradient,
                null
            )
        )



        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }


    override fun onResume() {
        super.onResume()
        if (isRestart) {
            (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                resources.getDrawable(R.drawable.action_bar_gradient, null)
            )

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


            //先检查权限，sdk28之前需要请求存储权限
            val needPermission =
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                        (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED)

            if (needPermission) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                //api >= 29 或者 已获取存储权限
                savePhoto()
            }

        }


        requireActivity().window.decorView.setOnSystemUiVisibilityChangeListener { systemUiVisibility ->
            val height = bottom_bar.height.toFloat()
            if (systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                ViewCompat.animate(bottom_bar).translationY(0f)
            } else {
                ViewCompat.animate(bottom_bar).translationY(height)
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodeForWriteExternalStorage -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePhoto()
                } else {
                    Toast.makeText(requireContext(), "获取存储权限失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            resources.getDrawable(
                R.color.colorPrimary,
                null
            )
        )
        disableFullscreen()
        isRestart = true
    }



    private fun savePhoto() {
        bitmap?.let {
            val photoName = requireArguments().get("url").toString().substringAfterLast("/")
            mainViewModel.savePhoto(it, photoName) { result ->
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }
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

