package com.szw.framelibrary.base

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.hwangjr.rxbus.RxBus
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.SZWUtils
import com.szw.framelibrary.view.CustomProgress
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


/**
 * Created by Swain
 * on 2017/1/16.
 */
@RuntimePermissions
abstract class BaseActivity : AppCompatActivity(), AbsBaseActivity {
    lateinit var mContext: Context

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
        //            finish();
        //            return;
        //        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (setInflateId() == 0)
            setContentView(setInflateId())
        else
            setContentView(setInflateView())

        mContext = this
        //        ButterKnife.bind(this);
        try {
            init()
            init(savedInstanceState)
            initToolbar()
            RxBus.get().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        SZWUtils.security()
    }

    override fun setInflateView(): View {
        return View(this)
    }
    /**
     * 视图，组件,数据的初始化
     */
    @Throws(Exception::class)
    override fun init() {

    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun permissionCameraWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) {
        permissionCameraWithPermissionCheck(intent, requestCode, isService)
    }

    override fun permissionCameraWithCheck(intent: Intent?, isService: Boolean) {
        permissionCameraWithPermissionCheck(intent, -1, isService)
    }

    override fun permissionLocationWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) {
        permissionLocationWithPermissionCheck(intent, requestCode, isService)
    }

    override fun permissionLocationWithCheck(intent: Intent?, isService: Boolean) {
        permissionLocationWithPermissionCheck(intent, -1, isService)
    }

    override fun permissionSMSWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) {
        permissionSMSWithPermissionCheck(intent, requestCode, isService)
    }

    override fun permissionSMSWithCheck(intent: Intent?, isService: Boolean) {
        permissionSMSWithPermissionCheck(intent, -1, isService)
    }

    override fun permissionCallPhoneWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) {
        permissionCallPhoneWithPermissionCheck(intent, requestCode, isService)
    }

    override fun permissionCallPhoneWithCheck(intent: Intent?, isService: Boolean) {
        permissionCallPhoneWithPermissionCheck(intent, -1, isService)
    }

    override fun permissionWAndRStorageWithCheck(intent: Intent?, isService: Boolean) {
        permissionWAndRWithPermissionCheck(intent, -1, isService, Runnable { })
    }

    override fun permissionWAndRStorageWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) {
        permissionWAndRWithPermissionCheck(intent, requestCode, isService, Runnable { })
    }

    override fun permissionWAndRStorageWithCheck(listener: Runnable) {
        permissionWAndRWithPermissionCheck(null, -1, false, listener)
    }

    @NeedsPermission(CAMERA, WRITE_EXTERNAL_STORAGE)
    fun permissionCamera(intent: Intent?, requestCode: Int, isService: Boolean) {
        startAction(intent, isService, if (requestCode == -1) Constants.Permission.Camera else requestCode)
    }

    @NeedsPermission(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    fun permissionLocation(intent: Intent?, requestCode: Int, isService: Boolean) {
        startAction(intent, isService, if (requestCode == -1) Constants.Permission.Location else requestCode)
    }

    @NeedsPermission(RECEIVE_SMS, READ_SMS)
    fun permissionSMS(intent: Intent?, requestCode: Int, isService: Boolean) {
        startAction(intent, isService, if (requestCode == -1) Constants.Permission.SMS else requestCode)
    }

    @NeedsPermission(CALL_PHONE)
    fun permissionCallPhone(intent: Intent?, requestCode: Int, isService: Boolean) {
        startAction(intent, isService, if (requestCode == -1) Constants.Permission.Phone else requestCode)
    }

    @NeedsPermission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    fun permissionWAndR(intent: Intent?, requestCode: Int, isService: Boolean, listener: Runnable) {
        listener.run()
        startAction(intent, isService, if (requestCode == -1) Constants.Permission.Phone else requestCode)
    }

    fun startAction(intent: Intent?, isService: Boolean, requestCode: Int) {
        if (intent != null) {
            if (isService)
                startService(intent)
            else
                startActivityForResult(intent, requestCode)
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }


    public override fun onDestroy() {

        try {
            RxBus.get().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        CustomProgress.disMissNow()
        super.onDestroy()
    }
}
