package com.szw.framelibrary.view.preview

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.szw.framelibrary.R
import com.szw.framelibrary.base.BaseActivity
import me.drakeet.materialdialog.MaterialDialog
import java.util.*


/**
 * Created by 史忠文
 * on 2017/6/9.
 */

class PreviewActivity<T : PreviewObject> : BaseActivity(), View.OnClickListener, PreviewFragment.LoadImageLister {
    // Content View Elements

    private lateinit var mToolbar: Toolbar
    private lateinit var toolbarLay: RelativeLayout
    private lateinit var parentLay: RelativeLayout
    private lateinit var mMTitle: TextView
    private lateinit var mMRight: TextView
    private lateinit var mMRightImg: ImageView
    private lateinit var mPreviewViewpager: ViewPager
    private lateinit var mLeftImg: ImageView
    private lateinit var mBtnSave: Button
    private var position: Int = 0
    private lateinit var fragments: MutableList<Fragment>
    private var imgUrls = ArrayList<T>()
    private var imgUrlsStrs = ArrayList<String>()
    private var resultPosition = ArrayList<Any>()
    private lateinit var mPagerAdapter: FragmentStatePagerAdapter
    private var isStrs: Boolean = false


    override fun initToolbar(){
        mToolbar.setContentInsetsAbsolute(0, 0)
        mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_previewActivity_toolBar_bg))
        mMTitle.textSize = 18f
        mMTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_previewActivity_toolBar_title))
        mMRightImg.setImageResource(R.drawable.ic_delete_black_24dp)
        mMRightImg.setPadding(SizeUtils.dp2px(15f), SizeUtils.dp2px(15f), SizeUtils.dp2px(15f), SizeUtils.dp2px(15f))
        setSupportActionBar(mToolbar)
    }

    override fun setInflateId(): Int = R.layout.activity_preview

    @Throws(Exception::class)
    override fun init() {
        permissionWAndRStorageWithCheck(Runnable {
            bindViews()
            iniEvent()
            initFragment()
            initPager()
        })
    }

    private fun iniEvent() {
        mMRightImg.visibility = if (intent.getBooleanExtra(PREVIEW_INTENT_IS_CAN_DELETE, false)) View.VISIBLE else View.GONE
        mMTitle.visibility = if (intent.getBooleanExtra(PREVIEW_INTENT_SHOW_NUM, false)) View.VISIBLE else View.GONE
        mToolbar.setNavigationOnClickListener { finish() }
        mMRightImg.setOnClickListener(this)
        mLeftImg.setOnClickListener(this)
        mBtnSave.setOnClickListener(this)
        val animatorExit = ObjectAnimator.ofFloat(parentLay, "translationY", 0f, SizeUtils.dp2px(-55f).toFloat())
        val animatorIn = ObjectAnimator.ofFloat(parentLay, "translationY", SizeUtils.dp2px(-55f).toFloat(), 0f)
        val transition = LayoutTransition()
        transition.setAnimator(LayoutTransition.APPEARING, animatorIn)
        transition.setAnimator(LayoutTransition.DISAPPEARING, animatorExit)
        parentLay.layoutTransition = transition
    }

    private fun initFragment() {
        resultPosition = ArrayList()
        fragments = ArrayList()
        if (intent.getParcelableArrayListExtra<Parcelable>(PREVIEW_INTENT_IMAGES) != null && intent.getParcelableArrayListExtra<Parcelable>(PREVIEW_INTENT_IMAGES).size > 0 && intent.getParcelableArrayListExtra<Parcelable>(PREVIEW_INTENT_IMAGES)[0] is PreviewObject) {
            imgUrls = intent.getParcelableArrayListExtra(PREVIEW_INTENT_IMAGES)
            isStrs = false
            (imgUrls as ArrayList<T>?)?.forEach { imgUrl ->
                val fragment = PreviewFragment.Instance(imgUrl.normalUrl, imgUrl.thumbnailUrl)
                fragment.setLoadImageLister(this)
                (fragments as ArrayList<Fragment>).add(fragment)
            }

        } else {
            imgUrlsStrs = intent.getStringArrayListExtra(PREVIEW_INTENT_IMAGES)
            isStrs = true
            imgUrlsStrs.forEach { imgUrl ->
                val fragment = PreviewFragment.Instance(imgUrl, "")
                fragment.setLoadImageLister(this)
                (fragments as ArrayList<Fragment>).add(fragment)
            }
        }
    }

    private fun initPager() {
        mPagerAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        }
        mPreviewViewpager.offscreenPageLimit = fragments.size
        mPreviewViewpager.adapter = mPagerAdapter
        mPreviewViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mMTitle.text = String.format("${(position + 1)}/${fragments.size}")
                this@PreviewActivity.position = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mMTitle.text = String.format("1/%s", fragments.size)
        mPreviewViewpager.setCurrentItem(intent.getIntExtra(PREVIEW_INTENT_POSITION, 0), false)
    }

    private fun bindViews() {
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbarLay = findViewById<View>(R.id.toolbarLay) as RelativeLayout
        parentLay = findViewById<View>(R.id.parentLay) as RelativeLayout
        mMTitle = findViewById<View>(R.id.mTitle) as TextView
        mMRight = findViewById<View>(R.id.mRight) as TextView
        mMRightImg = findViewById<View>(R.id.mRightImg) as ImageView
        mPreviewViewpager = findViewById<View>(R.id.preview_viewpager) as ViewPager
        mLeftImg = findViewById<View>(R.id.mLeftImg) as ImageView
        mBtnSave = findViewById<View>(R.id.btn_save) as Button
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.mLeftImg) {
            finish()
        }
        if (i == R.id.mRightImg) {
            delete()
        } else if (i == R.id.btn_save) {
            (mPagerAdapter.getItem(position) as PreviewFragment).saveImage()
        }
    }

    private fun delete() {
        val materialDialog = MaterialDialog(this)
        materialDialog.setTitle("提示").setPositiveButton("确定") {
            materialDialog.dismiss()
            val intent = Intent()
            if (isStrs) {
                resultPosition.add(imgUrlsStrs[position])
                imgUrlsStrs.removeAt(position)
                intent.putStringArrayListExtra(PREVIEW_INTENT_RESULT, resultPosition as ArrayList<String>)
            } else {
                resultPosition.add(imgUrls[position])
                imgUrls.removeAt(position)
                intent.putParcelableArrayListExtra(PREVIEW_INTENT_RESULT, resultPosition as ArrayList<out Parcelable>)
            }
            fragments.removeAt(position)
            if (position + 1 < fragments.size) {
                mMTitle.text = String.format("${(position + 1)}/${fragments.size}")
            } else {
                mMTitle.text = String.format("${fragments.size}/${fragments.size}")
            }

            mPagerAdapter.notifyDataSetChanged()
            setResult(Activity.RESULT_OK, intent)
            if (fragments.size == 0) {
                this@PreviewActivity.finish()
            }
        }.setNegativeButton("取消") { materialDialog.dismiss() }.setMessage("要删除这张照片吗？").show()
    }

    override fun complete() {
        mBtnSave.visibility = View.VISIBLE
    }

    override fun failed() {
        mBtnSave.visibility = View.GONE
    }

    override fun onClick() {
        toolbarLay.visibility = if (toolbarLay.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        mBtnSave.visibility = if (mBtnSave.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    companion object {

        // End Of Content View Elements

        val PREVIEW_INTENT_IMAGES = "PREVIEW_INTENT_IMAGES"//图片数据  可以传List<String>也可以传List<?extend PreviewObject> 重写里面方法
        val PREVIEW_INTENT_POSITION = "PREVIEW_INTENT_POSITION"//图片数据中所在的位置
        val PREVIEW_INTENT_IS_CAN_DELETE = "PREVIEW_INTENT_IS_CAN_DELETE"//是否可删除
        val PREVIEW_INTENT_SHOW_NUM = "PREVIEW_INTENT_SHOW_NUM"//是否显示1/4
        val PREVIEW_INTENT_RESULT = "PREVIEW_INTENT_RESULT"//删除的图片集合
    }
}
