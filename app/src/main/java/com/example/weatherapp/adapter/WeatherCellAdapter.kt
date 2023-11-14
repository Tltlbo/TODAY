package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.databinding.ListCellWeatherBinding

class exWeatherAdapter(val context: Context ,val itemList : MutableList<ModelWeather>) : RecyclerView.Adapter<exWeatherAdapter.WeatherViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): exWeatherAdapter.WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cell_weather,parent,false)
        return WeatherViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener : OnItemClickListener? = null

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.weatherimage.setImageResource(getRainImage(itemList[position].rainType, itemList[position].sky))
        holder.addressname.text = itemList[position].address
        holder.humidity.text = "습도 " + itemList[position].humidity + "%"
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun updateList() {
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val weatherimage = itemView.findViewById<ImageView>(R.id.imageWeather)
        val addressname = itemView.findViewById<TextView>(R.id.address)
        val humidity = itemView.findViewById<TextView>(R.id.tvHumidity)

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
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
