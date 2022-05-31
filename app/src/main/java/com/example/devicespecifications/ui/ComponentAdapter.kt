package com.example.devicespecifications.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devicespecifications.R
import com.example.devicespecifications.data.Component

/**
 * Created by Andronicus Kim on 5/31/22
 */
class ComponentAdapter(
    private val items: List<Component>,
    private val onComponentClick: OnComponentClick
) : RecyclerView.Adapter<ComponentAdapter.MyItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_component,parent,false)
        return MyItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) {
        val item = items[position]
        holder.ivIcon.setImageResource(item.resId)
        holder.tvName.text = item.name.lowercase().replaceFirstChar { it.uppercase() }
        holder.itemView.setOnClickListener {
            // pass the selected component to the listener
            onComponentClick.onClick(items[holder.adapterPosition].name)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }

    interface OnComponentClick{
        fun onClick(name: String)
    }
}