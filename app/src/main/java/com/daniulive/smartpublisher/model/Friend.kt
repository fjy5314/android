package com.daniulive.smartpublisher.model

import com.daniulive.smartpublisher.common.Pinyin4jUtil

/**
 * Created by flny on 2018/1/24.
 */
data class Friend(
        var id:Int?,
        var name: String,
        var phone: String? = null,
        var organization: String ? = null,
        var wechat: String? = null,
        var address: String? = null,
        var job: String? = null,
        var mobile1: String? = null,
        var mobile2: String? = null,
        var headerUrl: String? = null,
        var pinyin: String = Pinyin4jUtil.convertToSpell(name)

) {
    fun getFirstPinyin(): String {
        var tmp=pinyin.substring(0, 1)
        if(tmp.matches("[a-zA-Z]".toRegex())){
            return tmp.toUpperCase()
        }else{
            return "#"
        }

    }
}