package com.example.carsharing.lobby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carsharing.PostDetail
import com.example.carsharing.R

class PostAdapter: RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    val list : MutableList<PostDetail> = arrayListOf()
    private var sendListener : ItemClickListener? = null

    interface ItemClickListener{
        fun toClick(item: PostDetail)
    }
    fun setToClick(listener: ItemClickListener){
        sendListener = listener
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
        val resource = view.findViewById<TextView>(R.id.tv_resoucre)
        val title = view.findViewById<TextView>(R.id.tv_title)

        fun bind(item: PostDetail){


        }
    }
    fun update(newList: MutableList<PostDetail>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}