package com.example.carsharing.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carsharing.R
import com.example.carsharing.SearchDetails
import kotlinx.android.synthetic.main.post_itemview.view.*

class SearchAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var unAssignList = mutableListOf<SearchDetails>()
        private var listener:mItemClickListener? = null         //本地listener
        fun setToClick(listener:mItemClickListener){         //fragment會呼叫此方法並在那邊override toClick這個方法 好讓下面的setOnClickListener可以用
            this.listener = listener                        //把參數listener指定給本地listener
        }
    }

    interface mItemClickListener{
        fun toClick(items:SearchDetails)
    }

    inner class SearchViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){

        val resource = itemView.tv_type
        val title = itemView.tv_title
        val itemLayout = itemView.layout_item
        fun bind(item:SearchDetails){
            title.text = item.subject

            if(item.type==1){
                resource.text = "站內"
                resource.setTextColor(Color.rgb(202, 62, 71))
                resource.setBackgroundResource(R.drawable.ic_bottom_shape2)
            }
            else {
                resource.text = "批踢踢"
                resource.setBackgroundResource(R.drawable.ic_bottom_shape)
                resource.setTextColor(Color.rgb(36, 142, 169))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val example = inflater.inflate(R.layout.post_itemview, parent, false)
        return SearchViewHolder(example)

    }

    override fun getItemCount() = unAssignList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SearchViewHolder) {
            holder.bind(unAssignList[position])
            holder.itemLayout.setOnClickListener{
                listener?.toClick(unAssignList[position])
            }
        }

    }
}