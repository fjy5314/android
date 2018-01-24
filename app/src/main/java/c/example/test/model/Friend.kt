package c.example.test.model

/**
 * Created by flny on 2018/1/24.
 */
data class Friend (
     var  remark: String ,
    var  organization:String ,
     var account: String? = null,
     var name: String? = null,
     var phoneNumber: String? = null,
     var area: String? = null,
     var headerUrl: String? = null,
     var pinyin: String

     ){
    fun getFirstPinyin(): String {
        return if (pinyin != null) pinyin.substring(0, 1) else ""
    }
}