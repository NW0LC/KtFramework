package com.szw.framelibrary.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.szw.framelibrary.R


open class UpLoadProgress : Dialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    fun setMessage(message: CharSequence?) {
        if (message != null && message.isNotEmpty()) {
            findViewById<View>(R.id.message).visibility = View.VISIBLE
            val txt = findViewById<View>(R.id.message) as TextView
            txt.text = message
            txt.invalidate()
        }
    }

    companion object {


        var sProgress: UpLoadProgress? = null
        private var thread: Thread? = null

        /**
         * 弹出自定义ProgressDialog
         *
         * @param context        上下文
         * @param message        提示
         * @param cancelable     是否按返回键取消
         * @param cancelListener 按下返回键监听
         * @return
         */
        fun show(context: Context, message: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): UpLoadProgress? {
            if (thread != null && thread?.isAlive == true) {
                thread?.interrupt()
                return sProgress
            }

            if (sProgress != null && sProgress?.isShowing == true) {
                return sProgress
            }
            sProgress = UpLoadProgress(context, R.style.Custom_Progress)
            sProgress?.setTitle("")
            sProgress?.setContentView(R.layout.upload_prograss_dialog_layout)
            if (message == null || message.isEmpty()) {
                sProgress?.findViewById<View>(R.id.message)?.visibility = View.GONE
            } else {
                val txt = sProgress?.findViewById<View>(R.id.message) as TextView
                txt.text = message
            }
            // 按返回键是否取消
            sProgress?.setCancelable(cancelable)
            sProgress?.setCanceledOnTouchOutside(false)
            // 监听返回键处理
            if (cancelListener != null) {
                sProgress?.setOnCancelListener(cancelListener)
            }
            // 设置居中
            sProgress?.window?.attributes?.gravity = Gravity.CENTER
            val lp = sProgress?.window?.attributes
            // 设置背景层透明度
            lp?.dimAmount = 0.2f
            sProgress?.window?.attributes = lp
            // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            sProgress?.show()
            return sProgress
        }


        /**
         * 关闭dialog
         */
        fun disMiss() {
            if (sProgress != null && sProgress?.isShowing == true) {
                thread = Thread(Runnable {
                    try {
                        Thread.sleep(500)
                        sProgress?.dismiss()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                })
                thread?.start()

            }
        }

        /**
         * 关闭dialog
         */
        fun disMissNow() {
            if (sProgress != null && sProgress?.isShowing == true) {
                sProgress?.dismiss()
            }
        }
    }
}