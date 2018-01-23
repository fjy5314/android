package c.example.test.model

/**
 * Created by flny on 2018/1/19.
 */
data class BaseCard(
        val type: Int,
        var title: String?=null,
        var endTime: String?=null,
        var content: String?=null,
        var key: String?=null
)