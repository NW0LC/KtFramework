package com.example.ktframework

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
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
import android.os.Environment
import android.view.View
import com.blankj.utilcode.util.FileIOUtils
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun permissionWAndR() {
        // 加载图片
//         setup Glide request without the into() method
        val imgUrl = "https://img.alicdn.com/imgextra/i3/2688814083/TB2CKmSacbI8KJjy1zdXXbe1VXa_!!2688814083.jpg"

        Glide.with(this).load(imgUrl).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                val bitmap = ImageUtils.drawable2Bitmap(resource)
                if (isNeedCloseHardwareAcceleration(bitmap.width,bitmap.height)) {
                    photoView.visibility= View.VISIBLE
                    image.visibility= View.GONE
                    val path = cacheDir.path + "/$packageName"+
                            imgUrl.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[imgUrl.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
                    if (FileIOUtils.writeFileFromBytesByStream(path
                                    ,ImageUtils.bitmap2Bytes(bitmap,Bitmap.CompressFormat.JPEG))) {
                        photoView.setImage(ImageSource.uri(File(path).path))
                    }
                }else{
                    photoView.visibility= View.GONE
                    image.visibility= View.VISIBLE
                    image.setImageBitmap(bitmap)
                }
                return false
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).submit() // 加载图片

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utils.init(this)
        photoView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        photoView.minScale = 1.0F
        permissionWAndRWithPermissionCheck()
    }

    //added by Jack for handle exception "Bitmap too large to be uploaded into a texture".
    fun isNeedCloseHardwareAcceleration(w: Int, h: Int): Boolean {
        val maxSize = IntArray(1)
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0)

        return maxSize[0] < h || maxSize[0] < w

    }
}
