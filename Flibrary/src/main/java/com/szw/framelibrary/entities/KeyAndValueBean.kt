package com.szw.framelibrary.entities
/**
 * Created by 史忠文
 * on 2017/8/9.
 */

abstract class KeyAndValueBean{
    open var key: String=""
        get() = absKey()
    var value=""
        get() = absValue()
    var isCheck=false

    abstract fun absKey():String
    abstract fun absValue():String
}