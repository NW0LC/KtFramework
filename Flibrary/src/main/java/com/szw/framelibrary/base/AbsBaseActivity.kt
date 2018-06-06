package com.szw.framelibrary.base

import android.content.Intent
import android.os.Bundle
import android.view.View

/**
 * Created by 史忠文
 * on 2017/3/20.
 */

internal interface AbsBaseActivity {
    /**
     * 视图，组件,数据的初始化
     */
    @Throws(Exception::class)
    fun init()

    fun init(savedInstanceState: Bundle?)
    /**
     * 初始化电量条和状态栏
     */
    fun initToolbar()
    /**
     * 设置布局id
     */
    fun setInflateId(): Int
    /**
     * 设置布局View
     */
    fun setInflateView(): View

    /**
     * 设置权限
     * BaseActivityPermissionsDispatcher.showCameraWithCheck(this);
     * BaseActivityPermissionsDispatcher.locationAndSMSWithCheck(this);
     * BaseActivityPermissionsDispatcher.callPhoneWithCheck(this);
     */
    fun permissionWAndRStorageWithCheck(listener:Runnable) //读写权限
    fun permissionWAndRStorageWithCheck(intent: Intent?, isService: Boolean) //读写权限
    fun permissionWAndRStorageWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //读写权限
    fun permissionCameraWithCheck(intent: Intent?, isService: Boolean) //拍照与读取内存卡，并启动意图，null 不启动
    fun permissionCameraWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //拍照与读取内存卡，并启动意图，null 不启动
    fun permissionLocationWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun permissionLocationWithCheck(intent: Intent?, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun permissionCallPhoneWithCheck(intent: Intent?, isService: Boolean) //电话，并启动意图，null 不启动
    fun permissionCallPhoneWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //电话，并启动意图，null 不启动
    fun permissionSMSWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun permissionSMSWithCheck(intent: Intent?, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
}
