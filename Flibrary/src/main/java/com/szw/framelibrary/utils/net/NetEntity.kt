package com.szw.framelibrary.utils.net

//@HttpResponse(parser = JsonResponseParser.class)//每一个实体类必须加这句话，别忘了在baseactivity里面 初始化注解 x.view().inject(this);
class NetEntity<T> : AbsNetBean {
    override var success = false
    override var error = ""
    var result: T? = null
    override fun getCode(): Int {
        return if (success)
            200
        else
            super.getCode()
    }
}
