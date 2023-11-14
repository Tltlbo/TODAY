import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ModelDust(
    val response: DustResponse
)

data class DustResponse(
    val body: DustBody,
    val header: DustHeader
)

@Parcelize
data class DustItem(
    val pm10Grade: String = "",
    val pm10Value: String = "",
    val pm25Grade: String = "",
    val pm25Value: String = "",
    var address : String = "",
    var stationName : String = "",
) : Parcelable

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