package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.weatherapp.R
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.databinding.ListCellWeatherBinding

class WeatherCellAdapter(private val context : Context, private val items : MutableList<ModelWeather>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ModelWeather = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ListCellWeatherBinding.inflate(LayoutInflater.from(context))
        val item : ModelWeather = items[position]
        binding.imageWeather.setImageResource(getRainImage(item.rainType, item.sky))
        binding.tvTemp.text = item.temp + "°"
        binding.address.text = item.address
        binding.tvHumidity.text = item.humidity

        return binding.root
    }

    fun getRainImage(rainType : String, sky: String) : Int {
        return when(rainType) {
            "0" -> getWeatherImage(sky)
            "1" -> R.drawable.rainy
            "2" -> R.drawable.hail
            "3" -> R.drawable.snowy
            "4" -> R.drawable.brash
            else -> getWeatherImage(sky)
        }
    }

    fun getWeatherImage(sky : String) : Int {
        // 하늘 상태
        return when(sky) {
            "1" -> R.drawable.sun                       // 맑음
            "3" ->  R.drawable.cloudy                     // 구름 많음
            "4" -> R.drawable.blur                 // 흐림
            else -> R.drawable.ic_launcher_foreground   // 오류
        }
    }
}
