package com.szw.framelibrary.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Created by 史忠文
 * on 2017/10/26.
 */

class DividerGridItemDecoration : RecyclerView.ItemDecoration {
    lateinit var mDivider: Drawable
    private lateinit var mPaint: Paint
    private var mDividerHeight = 2

    constructor(context: Context) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /** * 自定义分割线
     *
     * @param context
     * @param drawableId 分割线图片
     */
    constructor(context: Context, drawableId: Int) {

        mDivider = ContextCompat.getDrawable(context, drawableId)!!
        mDividerHeight = mDivider.intrinsicHeight
    }

    /** * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, dividerHeight: Int, dividerColor: Int) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = context.resources.getColor(dividerColor)
        mPaint.style = Paint.Style.FILL
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: State?) {
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun getSpanCount(parent: RecyclerView?): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent?.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    // 绘制水平线
    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin + mDividerHeight
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDividerHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    // 绘制垂直线
    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.right + params.rightMargin
            val right = left + mDividerHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    // 判断是否是最后一列
    private fun isLastColum(parent: RecyclerView?, pos: Int, spanCount: Int, childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent?.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)
            // 如果是最后一列，则不需要绘制右边
            {
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {
                    // 如果是最后一列，则不需要绘制右边
                    return true
                }
            } else {
                childCount -= childCount % spanCount
                if (pos >= childCount)
                // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    // 判断是否是最后一行
    private fun isLastRaw(parent: RecyclerView?, pos: Int, spanCount: Int, childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent?.layoutManager
        if (layoutManager is GridLayoutManager) {
            childCount -= childCount % spanCount
            if (pos >= childCount)
            // 如果是最后一行，则不需要绘制底部
                return true
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount -= childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView?) {
        val spanCount = getSpanCount(parent)
        val childCount = parent?.adapter?.itemCount?:0
        when {
            isLastRaw(parent, itemPosition, spanCount, childCount)
                // 如果是最后一行，则不需要绘制底部
            -> outRect.set(0, 0, mDividerHeight, 0)
            isLastColum(parent, itemPosition, spanCount, childCount)
                // 如果是最后一列，则不需要绘制右边
            -> outRect.set(0, 0, 0, mDividerHeight)
            else -> outRect.set(0, 0, mDividerHeight, mDividerHeight)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
