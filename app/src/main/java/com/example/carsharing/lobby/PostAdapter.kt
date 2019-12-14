package com.example.carsharing.lobby

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carsharing.AllpostsDetails
import com.example.carsharing.R

class PostAdapter: RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    val list : MutableList<AllpostsDetails> = arrayListOf()
    private var clickListener : ItemClickListener? = null

    interface ItemClickListener{
        fun toClick(item: AllpostsDetails)
    }
    fun setToClick(listener: ItemClickListener){
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_itemview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tv_type = view.findViewById<TextView>(R.id.tv_type)
        val tv_title = view.findViewById<TextView>(R.id.tv_title)

        fun bind(item: AllpostsDetails){
            tv_title.text = item.subject
            if (item.type == 1){
                tv_type.setTextColor(Color.rgb(202, 62, 71))
                tv_type.text = "站內"
                tv_type.setBackgroundResource(R.drawable.ic_bottom_shape2)
            }else{
                tv_type.text = "批踢踢"
                tv_type.setBackgroundResource(R.drawable.ic_bottom_shape)
                tv_type.setTextColor(Color.rgb(36, 142, 169))
            }
            itemView.setOnClickListener {
                clickListener?.toClick(item)
            }
        }
    }
    fun update(newList: MutableList<AllpostsDetails>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}