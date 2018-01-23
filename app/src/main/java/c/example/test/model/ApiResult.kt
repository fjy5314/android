package c.example.test.model

data class ApiResult<T>(
        val retCode: String,
        var retMsg: String? = null,
        var data: T? = null
)
