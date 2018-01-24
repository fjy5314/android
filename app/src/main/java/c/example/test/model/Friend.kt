package c.example.test.model

import c.example.test.common.Pinyin4jUtil

/**
 * Created by flny on 2018/1/24.
 */
data class Friend(
        var name: String,
        var organization: String,
        var phoneNumber: String,
        var pinyin: String = Pinyin4jUtil.convertToSpell(name),
        var remark: String? = null,
        var account: String? = null,
        var area: String? = null,
        var headerUrl: String? = null

) {
    fun getFirstPinyin(): String {
        return pinyin.substring(0, 1)
    }
}