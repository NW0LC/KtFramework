package com.szw.framelibrary.utils.net

interface AbsNetBean{
    var result: String
    var messError: String
    fun getCode(): Int = Integer.valueOf(if (result.isNotEmpty())result else "0")
}
