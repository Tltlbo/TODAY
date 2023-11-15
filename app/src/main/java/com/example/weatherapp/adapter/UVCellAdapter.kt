package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.UVItem

class UVAdapter(val context: Context, val itemList : MutableList<UVItem>) : RecyclerView.Adapter<UVAdapter.UVViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UVAdapter.UVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cell_uv,parent,false)
        return UVViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener : OnItemClickListener? = null

    override fun onBindViewHolder(holder: UVViewHolder, position: Int) {
        holder.uvimage.setImageResource(getUVImage(itemList[position].maxUV))
        holder.address.text = itemList[position].address
        holder.uvindex.text = "자외선 지수: " + itemList[position].maxUV
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun updateList() {
        notifyDataSetChanged()
    }

    inner class UVViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val uvimage = itemView.findViewById<ImageView>(R.id.imageUV)
        val address = itemView.findViewById<TextView>(R.id.address)
        val uvindex = itemView.findViewById<TextView>(R.id.UVIndex)

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    fun getUVImage(uv : Int) : Int {
        if(uv <= 2) { return R.drawable.bestuv }
        else if (uv <= 5) {return R.drawable.gooduv}
        else if (uv <= 7) {return R.drawable.betteruv}
        else if (uv <= 10) {return R.drawable.baduv }
        else {return R.drawable.terroruv}
    }
}