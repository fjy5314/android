package c.example.test.model

/**
 * Created by flny on 2018/1/16.
 */
  data class Law(
        var type: Int,
        var title: String,
        var endTime: String,
        var content: String,
        var category:Category,
        var name: String

)