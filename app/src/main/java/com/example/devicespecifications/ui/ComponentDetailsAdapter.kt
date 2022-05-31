package com.example.devicespecifications.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devicespecifications.R
import com.example.devicespecifications.data.ComponentDetail

/**
 * Created by Andronicus Kim on 5/31/22
 */
class ComponentDetailsAdapter(
    private val items: List<ComponentDetail>
) : RecyclerView.Adapter<ComponentDetailsAdapter.MyItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_component_details,parent,false)
        return MyItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
    }
}