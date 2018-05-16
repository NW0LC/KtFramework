package com.example.ktframework

import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlinx.android.synthetic.main.activity_main.*
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES10




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utils.init(this)
        image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)
        image.minScale = 1F

        Glide.with(this).load("http://wx2.sinaimg.cn/mw690/5ffb33dcgy1fih6nytd4kj20sg2dcqli.jpg").listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                ToastUtils.showShort("onLoadFailed")
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                val bitmap = ImageUtils.drawable2Bitmap(resource)
                if (!isNeedCloseHardwareAcceleration(bitmap.width,bitmap.height)) {
                    image.setImage(ImageSource.bitmap(bitmap))
                }
                return false
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).submit()// 加载图片
    }

    //added by Jack for handle exception "Bitmap too large to be uploaded into a texture".
    fun isNeedCloseHardwareAcceleration(w: Int, h: Int): Boolean {
        val maxSize = IntArray(1)
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0)

        return maxSize[0] < h || maxSize[0] < w

    }
}
