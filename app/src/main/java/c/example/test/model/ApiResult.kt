package c.example.test.model

data class ApiResult<T>(
        val retCode: String,
        var retMsg: String,
        var data: T
)
