package com.szw.framelibrary.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import com.szw.framelibrary.R
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.observer.SmsContentObserver
import ezy.boost.update.IUpdateChecker
import ezy.boost.update.IUpdateParser
import ezy.boost.update.UpdateInfo
import ezy.boost.update.UpdateManager
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern


/**
 * Created by 史忠文
 * on 2017/3/23.
 */

object SZWUtils {


    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun checkLogin(mContext: Activity, intent: Intent, clazz: Class<*>): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, clazz)
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else
            true
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    fun checkLogin(mContext: Activity, clazz: Class<*>): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, clazz)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else
            true
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    fun checkLogin(mContext: Fragment, clazz: Class<*>): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext.activity, clazz)
            mContext.startActivityForResult(login, 0xc8)
            mContext.activity?.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else
            true
    }

    /**
     * 获取单个App版本号
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppVersion(context: Context, packageName: String): String {
        val pManager = context.packageManager
        val packageInfo = pManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName
    }

    /**
     *
     * @param versionServer versionServer
     * @param versionLocal versionLocal
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    fun versionComparison(versionServer: String?, versionLocal: String?): Int {
        if (versionServer == null || versionServer.isEmpty() || versionLocal == null || versionLocal.isEmpty())
            throw IllegalArgumentException("Invalid parameter!")

        var index1 = 0
        var index2 = 0
        while (index1 < versionServer.length && index2 < versionLocal.length) {
            val number1 = getValue(versionServer, index1)
            Log.i(TAG, " ===== number1 ====" + Arrays.toString(number1))
            val number2 = getValue(versionLocal, index2)
            Log.i(TAG, " ===== number2 ====" + Arrays.toString(number2))

            when {
                number1[0] < number2[0] -> {
                    Log.i(TAG, " ===== number1[0] ====" + number1[0])
                    Log.i(TAG, " ===== number2[0] ====" + number2[0])
                    return -1
                }
                number1[0] > number2[0] -> {
                    Log.i(TAG, " ===== number1[0] ====" + number1[0])
                    Log.i(TAG, " ===== number2[0] ====" + number2[0])
                    return 1
                }
                else -> {
                    index1 = number1[1] + 1
                    index2 = number2[1] + 1
                }
            }
        }
        if (index1 == versionServer.length && index2 == versionLocal.length)
            return 0
        return if (index1 < versionServer.length)
            1
        else
            -1
    }

    /**
     *
     * @param version
     * @param index
     * the starting point
     * @return the number between two dots, and the index of the dot
     */
    private fun getValue(version: String, index: Int): IntArray {
        var index = index
        val valueIndex = IntArray(2)
        val sb = StringBuilder()
        while (index < version.length && version[index] != '.') {
            sb.append(version[index])
            index++
        }
        valueIndex[0] = Integer.parseInt(sb.toString())
        valueIndex[1] = index

        return valueIndex
    }

    /**
     * @param mContext 上下文
     * @param textView 返回验证码的textView
     * @return 验证码handler
     */
    fun patternCode(mContext: Context, textView: TextView,length:Int): Handler = MyHandler(mContext, textView,length)

    class MyHandler constructor(internal var mContext: Context, private var textView: TextView,private var length:Int) : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val outbox = msg.obj as String
            //            edCode.setText(outbox);
            //            Toast.makeText(mContext, outbox, Toast.LENGTH_SHORT).show();
            textView.text = SZWUtils.patternCode(outbox, length)
        }
    }


    /**
     * 注册读取短信observer
     *
     * @param context  上下文
     * @param mHandler 监听
     * @return observer
     */
    fun registerSMS(context: Context, mHandler: Handler): SmsContentObserver {
        //注册内容观察者获取短信
        val smsContentObserver = SmsContentObserver(context, mHandler)
        // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms
        // 监听不到其他的Uri 比如说 content://sms/outbox
        val smsUri = Uri.parse("content://sms")
        context.contentResolver.registerContentObserver(smsUri, true, smsContentObserver)
        return smsContentObserver
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent 短信
     * @return 验证码
     */
    private fun patternCode(patternContent: String): String? {
        val patternCoder = "(?<!\\d)\\d{7}(?!\\d)"//匹配6位验证码
        if (TextUtils.isEmpty(patternContent)) {
            return null
        }
        val p = Pattern.compile(patternCoder)
        val matcher = p.matcher(patternContent)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }

    /**
     * 从短信字符窜提取验证码
     * @param body 短信内容
     * @param length  验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    fun patternCode(body: String, length: Int): String? {
        // 首先([a-zA-Z0-9]{length})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{length})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{length})后面不能有数字出现


        //  获得数字字母组合
        //    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        //  获得纯数字
        val p = Pattern.compile("(?<![0-9])([0-9]{$length})(?![0-9])")

        val m = p.matcher(body)
        if (m.find()) {
            println(m.group())
            return m.group(0)
        }
        return null
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext    上下文
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    fun isServiceWork(mContext: Context, serviceName: String): Boolean {
        var isWork = false
        val myAM = mContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList = myAM.getRunningServices(40)
        if (myList.size <= 0) {
            return false
        }
        for (i in myList.indices) {
            val mName = myList[i].service.className
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }

    /**
     * @param index 间距
     * @param strs  字符串
     * @param str   插入字符
     * @return 插入字符后字符串
     */
    fun insert(index: Int, strs: String, str: String): String {
        var len = ""
        if (strs.length > index) {
            var oldIndex = 0
            var i = index
            while (i < strs.length) {
                len += strs.substring(oldIndex, i) + str
                oldIndex = i
                i += index
            }

            if (oldIndex + index >= strs.length)
                len += strs.substring(oldIndex)
        }
        return len
    }

    /**
     * @param color  需改变字体颜色
     * @param text    所有文字
     * @param keyword  需变色的文字
     * @return
     */
    fun matcherSearchTitle(color: Int, text: String, keyword: String): SpannableString {
        val string = text.toLowerCase()
        val key = keyword.toLowerCase()
        val pattern = Pattern.compile(key)
        val matcher = pattern.matcher(string)
        val ss = SpannableString(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            ss.setSpan(ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return ss
    }


    /**
     * 指定 文字 指定 颜色 变色
     */
    fun setColor(context: Context, text: String, text1: String, text2: String): CharSequence {
        val style = SpannableStringBuilder(text)
        // 关键字“孤舟”变色，0-text1.length()
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), 0, text1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 关键字“寒江雪”变色，text1.length() + 6-text1.length() + 6 + text2.length()
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), text1.length + 6, text1.length + 6 + text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return style
    }

    /**
     * @param html 富文本
     * @return  矫正图片后的富文本
     */
    fun getHtmlContent(html: String): String {
        //
        val doc_Dis = Jsoup.parse(html)
        val ele_Img = doc_Dis.getElementsByTag("img")
        if (ele_Img.size != 0) {
            for (e_Img in ele_Img) {
                e_Img.attr("style", "max-width:100%;height:auto;")
            }
        }
        //
        return doc_Dis.toString()
    }

    /**
     * [isManual] 是否是手动更新   true 是  false 否
     * [checkUrl] 检查接口
     * [postData] post参数  param=12&param2=32
     * [notifyId] 通知id
     */
    fun checkUpgrade(context:Context,isManual: Boolean,checkUrl:String,postData:String, notifyId: Int) {
        /**
         * 检查版本更新
         * 地址:	http://xxx/App/Upgrade
         * 入参:	versionCode	string	必填	版本号
         * 返回:	{
         *             "code":"200",
         *             "message":"提示"，
         *               "data": {
         *                     "hasUpdate":"false// 是否有新版本(最重要)",
         *                     "isSilent":"false// 是否静默下载：有新版本时不提示直接下载",
         *                     "isForce":"false// 是否强制安装：不安装无法使用app",
         *                     "isAutoInstall":"false// 是否下载完成后自动安装",
         *                     "isIgnorable":"true// 是否可忽略该版本",
         *                     "maxTimes":"0// 一天内最大提示次数，<1时不限",
         *                     "versionCode":101,
         *                     "versionName":"1.0.1",
         *                     "updateContent":"更新说明",
         *                     "url":"下载地址",
         *                     "md5":"文件md5校验码(最重要)",
         *                     "size":"文件长度"
         *               }
         *         }
         * 说明	备注	参数是布尔值的都有默认参数   颜色标记为必传，其他选传
         * md5	apk文件 的md5   不是keystore的md5
         */
        UpdateManager.create(context).setUrl(checkUrl).setPostData(postData).setManual(isManual).setNotifyId(notifyId).check()
    }
    /**
     *  不建议使用
     * [isManual] 是否是手动更新   true 是  false 否
     * [checkUrl] 检查接口
     * [postData] post参数  param=12&param2=32
     * [notifyId] 通知id
     */
    fun checkUpgrade(context:Context,isManual: Boolean,checkUrl:String,postData:String, notifyId: Int,checker:IUpdateChecker ) {
        UpdateManager.create(context).setChecker(checker).setUrl(checkUrl).setPostData(postData).setManual(isManual).setNotifyId(notifyId).check()
    }
    /**
     * 不建议使用
     * [isManual] 是否是手动更新   true 是  false 否
     * [checkUrl] 检查接口
     * [postData] post参数  param=12&param2=32
     * [notifyId] 通知id
     * [checker]  自定义  访问网络获取验证版本信息方法   agent.setInfo(String)
     * [parser]  自定义  解析获取验证版本信息方法  返回  UpdateInfo
     */
    fun checkUpgrade(context:Context,isManual: Boolean,checkUrl:String,postData:String, notifyId: Int,checker:IUpdateChecker , parser: IUpdateParser) {
        UpdateManager.create(context).setChecker(checker).setUrl(checkUrl).setPostData(postData).setManual(isManual).setNotifyId(notifyId).setParser(parser).check()
    }
    /**
     * 不建议使用
     * [isManual] 是否是手动更新   true 是  false 否
     * [checkUrl] 检查接口
     * [postData] post参数  param=12&param2=32
     * [notifyId] 通知id
     * [parser]  自定义  解析获取验证版本信息方法  返回  UpdateInfo
     */
    fun checkUpgrade(context:Context,isManual: Boolean,checkUrl:String,postData:String, notifyId: Int , parser: IUpdateParser) {
        UpdateManager.create(context).setUrl(checkUrl).setPostData(postData).setManual(isManual).setNotifyId(notifyId).setParser(parser).check()
    }

    /**
     * 版本更新解析
     * [s]
     */
    @Throws(JSONException::class)
    private fun checkUpgradeParse(s: String): UpdateInfo {
        val o = JSONObject(s)
        return parse(if (o.has("info")) o.getJSONObject("info") else o)
    }

    private fun parse(o: JSONObject?): UpdateInfo {
        val info = UpdateInfo()
        if (o == null) {
            return info
        }
        info.hasUpdate = o.optBoolean("hasUpdate", false)
        if (!info.hasUpdate) {
            return info
        }
        info.isSilent = o.optBoolean("isSilent", false)
        info.isForce = o.optBoolean("isForce", false)
        info.isAutoInstall = o.optBoolean("isAutoInstall", !info.isSilent)
        info.isIgnorable = o.optBoolean("isIgnorable", true)

        info.versionCode = o.optInt("android_VersionCode", 0)
        info.versionName = o.optString("android_VersionName")
        info.updateContent = o.optString("updateContent")

        info.url = o.optString("url")
        info.md5 = o.optString("md5")
        info.size = o.optLong("size", 0)

        return info
    }
}
