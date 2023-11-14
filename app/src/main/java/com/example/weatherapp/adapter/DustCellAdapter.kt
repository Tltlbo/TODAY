package com.example.weatherapp.adapter

import DustItem
import ModelDust
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.ModelWeather


    class DustAdapter(val context: Context, val itemList : MutableList<DustItem>) : RecyclerView.Adapter<DustAdapter.DustViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DustAdapter.DustViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cell_dust,parent,false)
            return DustViewHolder(view)
        }

        interface OnItemClickListener {
            fun onItemClick(position: Int) {}
        }

        var itemClickListener : OnItemClickListener? = null

        override fun onBindViewHolder(holder: DustViewHolder, position: Int) {
            holder.dustimage.setImageResource(getDustImage(itemList[position].pm10Value.toInt()))
            holder.addressname.text = itemList[position].address
            holder.dustindex.text = "미세먼지 지수: " + itemList[position].pm10Value + "㎍/㎥"
        }

        override fun getItemCount(): Int {
            return itemList.count()
        }

        fun updateList() {
            notifyDataSetChanged()
        }

        inner class DustViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            val dustimage = itemView.findViewById<ImageView>(R.id.imageDust)
            val addressname = itemView.findViewById<TextView>(R.id.address)
            val dustindex = itemView.findViewById<TextView>(R.id.dustindex)

            init {
                itemView.setOnClickListener {
                    itemClickListener?.onItemClick(adapterPosition)
                }
            }
        }


        fun getDustImage(pm10Value : Int) : Int {
            if(pm10Value <= 50) { return R.drawable.best }
            else if (pm10Value <= 100) {return R.drawable.good}
            else if (pm10Value <= 250) {return R.drawable.bad}
            else {return R.drawable.terror}
        }

    }
