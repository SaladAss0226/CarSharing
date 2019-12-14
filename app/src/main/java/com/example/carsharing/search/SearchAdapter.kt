package com.example.carsharing.search

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
            resource.text = if(item.ptt_url==null) "站內" else "批踢踢"
        }

    }

//    inner class GridViewHolder(itemView:View):
//        RecyclerView.ViewHolder(itemView){
//        val icon01 = itemView.imgv02
//        val name02 = itemView.tv_name02
//
//        fun bind(item:com.example.carsharing.SearchDetails){
//            icon.setImageResource(item.img)
//            name02.text = item.name
//        }
//
//    }

//    //判斷data其中一項屬性來判斷該使用哪種viewType
//    override fun getItemViewType(position: Int): Int {
//        return if(unAssignList[position].isGridView) 1 else 2
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {

//        return  when (viewType){
//            1->{
//                val inflater = LayoutInflater.from(parent.context)
//                val example = inflater.inflate(R.layout.example_grid_style, parent, false)
//                GridViewHolder(example)
//            }
//            else->{
//                val inflater = LayoutInflater.from(parent.context)
//                val example = inflater.inflate(R.layout.example_list_style, parent, false)
//                ListViewHolder(example)
//            }
//        }
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