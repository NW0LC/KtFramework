package com.szw.framelibrary.config

/**
 * Created by ${Swain}
 * on 2016/10/1.
 */
class Constants {
    /**
     * 刷新
     */
    object RefreshState {
       const val STATE_REFRESH = 0
        const val STATE_LOAD_MORE = 1
    }

    /**
     * 结果回执码
     */
    object Result {
        const val Intent_ClassName = "Intent_ClassName"
        const val Result_Login_Ok = 200
    }

    /**
     * 网络码
     */
    object NetCode {
        const val SUCCESS = 200
    }

    /**
     * 权限请求 requestCode
     */
    object Permission {
        const val Location = 100
        const val Phone = 200
        const val SMS = 300
        const val Camera = 400
    }

    /**
     * 回调
     */
    object BusAction {

        /**
         * 支付完成
         */
        const val Pay_Finish = "Pay_Finish"

    }

    /**
     * 定位
     */
    object Location {
        const val INTENT_ACTION_LOCATION = "intent_action_location"
        const val INTENT_DATA_LOCATION_CITY = "intent_data_location_city"
        const val INTENT_DATA_LOCATION_LONGITUDE = "intent_data_location_longitude"
        const val INTENT_DATA_LOCATION_LATITUDE = "intent_data_location_latitude"
    }
}
