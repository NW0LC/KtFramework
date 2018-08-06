package com.szw.framelibrary.utils.net

interface AbsNetBean{
    var error: String
    var success: Boolean
    fun getCode(): Int = if (!error.isEmpty()) 400 else 500
}
