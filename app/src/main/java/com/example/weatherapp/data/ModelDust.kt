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
    var pm10Value: String = "0",
    val pm25Grade: String = "",
    var pm25Value: String = "0",
    var address : String = "",
    var stationName : String = "",
    var x : Double = 0.0,
    var y : Double = 0.0,
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