package com.example.weatherapp.component


import android.content.Context
import android.graphics.Point
import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream


class Common {
    // baseTime 설정하기
    fun getBaseTime(h: String, m: String): String {
        var result = ""

        // 45분 전이면
        if (m.toInt() < 45) {
            // 0시면 2330
            if (h == "00") result = "2330"
            // 아니면 1시간 전 날씨 정보 부르기
            else {
                var resultH = h.toInt() - 1
                // 1자리면 0 붙여서 2자리로 만들기
                if (resultH < 10) result = "0" + resultH + "30"
                // 2자리면 그대로
                else result = resultH.toString() + "30"
            }
        }
        // 45분 이후면 바로 정보 받아오기
        else result = h + "30"

        return result
    }

    // 위경도를 기상청에서 사용하는 격자 좌표로 변환
    fun dfsXyConv(v1: Double, v2: Double): Point {
        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 투영 위도1(degree)
        val SLAT2 = 60.0        // 투영 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }

    fun readExcel(x: Int, y: Int, mContext: Context): String {
        try {
            var file : InputStream = mContext.resources.assets.open("nxnyaddress.xlsx")
            val workbook = XSSFWorkbook(file)

            val sheet = workbook.getSheetAt(0)

            //행을 반복할 변수 만들어주기
            val rowIter = sheet.rowIterator()
            //행 넘버 변수 만들기
            var rowno = 0


            //행 반복문
            while (rowIter.hasNext()) {
                val myRow = rowIter.next() as XSSFRow
                if (rowno != 0) {
                    //열을 반복할 변수 만들어주기
                    val cellIter = myRow.cellIterator()
                    //열 넘버 변수 만들기
                    var colno = 0
                    var addressfullname = ""
                    var xflag = 0
                    var yflag = 0
                    //열 반복문
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as XSSFCell
                        if (colno === 0) {//0번째 열이라면,
                            addressfullname += myCell.toString() + " "
                        } else if (colno === 1) {//1번째 열이라면,
                            addressfullname += myCell.toString() + " "
                        } else if (colno === 2) {//2번째 열이라면,
                            addressfullname += myCell.toString()
                        }
                        if (colno == 3 && myCell.toString().equals(x.toString())) { xflag = 1 }
                        if (colno == 4 && myCell.toString().equals(y.toString())) { yflag = 1 }

                        if (xflag == 1 && yflag == 1) {
                            return addressfullname
                        }
                        colno++
                    }
                }
                rowno++
            }

        } catch (e: Exception) {
            println("error")
        }
        return "fail"
    }
}