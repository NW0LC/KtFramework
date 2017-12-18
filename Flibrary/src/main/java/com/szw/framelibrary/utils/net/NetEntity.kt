package com.szw.framelibrary.utils.net

//@HttpResponse(parser = JsonResponseParser.class)//每一个实体类必须加这句话，别忘了在baseactivity里面 初始化注解 x.view().inject(this);
class NetEntity<T> :AbsNetBean{
    override var result=""
    override var messError=""
    var code=""
    var message= ""
    var data: T? = null
    var info: T? = null
    override fun getCode(): Int {
        return if (result.isEmpty())
            Integer.valueOf(if (code.isNotEmpty())code else "0")
        else
            super.getCode()
    }
}
