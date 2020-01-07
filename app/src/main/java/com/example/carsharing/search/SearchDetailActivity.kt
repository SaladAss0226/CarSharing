package com.example.carsharing.search

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.carsharing.R
import com.example.carsharing.search.SearchActivity.Companion.itemClicked
import kotlinx.android.synthetic.main.activity_search_detail.*


class SearchDetailActivity : AppCompatActivity() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        search_details_departure.text = itemClicked.departure
        search_detail_destination.text = itemClicked.destination
        search_detail_subject.text = itemClicked.subject
        search_detail_date.text = itemClicked.departure_date
        search_detail_seat.text = "剩餘${itemClicked.seat}個座位"
        search_detail_describe.text = itemClicked.description

        if(itemClicked.type==2){
            search_detail_seat.visibility = View.GONE
            search_imageView12.visibility = View.GONE
            search_constraint1.visibility = View.GONE
            btn_join_search.setText("前往原文連結")
            //動態設定margin
            val params = LinearLayout.LayoutParams(search_constraint2.getLayoutParams())
            params.setMargins(0, 700, 0, 0)
            search_constraint2.setLayoutParams(params)
        }

        search_details_back.setOnClickListener {
            this.finish()
        }
        btn_join_search.setOnClickListener {
            if(itemClicked.type==2){
                val uri: Uri = Uri.parse(itemClicked.ptt_url)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

        }
    }
}
