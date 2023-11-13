
data class ModelDust(
    val response: DustResponse
)

data class DustResponse(
    val body: DustBody,
    val header: DustHeader
)

data class DustItem(
    val coFlag: Any,
    val coGrade: String,
    val coValue: String,
    val dataTime: String,
    val khaiGrade: String,
    val khaiValue: String,
    val no2Flag: Any,
    val no2Grade: String,
    val no2Value: String,
    val o3Flag: Any,
    val o3Grade: String,
    val o3Value: String,
    val pm10Flag: Any,
    val pm10Grade: String,
    val pm10Value: String,
    val pm25Flag: Any,
    val pm25Grade: String,
    val pm25Value: String,
    val so2Flag: Any,
    val so2Grade: String,
    val so2Value: String
)

data class DustHeader(
    val resultCode: String,
    val resultMsg: String
)

data class DustBody(
    val items: List<DustItem>,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)