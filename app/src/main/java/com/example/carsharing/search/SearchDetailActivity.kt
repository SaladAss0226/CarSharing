package com.example.carsharing.search

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import com.example.carsharing.R
import com.example.carsharing.search.SearchActivity.Companion.itemClicked
import com.example.carsharing.search.SearchAdapter.Companion.unAssignList
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

        if(itemClicked.type==1) search_detail_url.text = ""
       else if(itemClicked.type==2){
            search_detail_url.setText(Html.fromHtml(itemClicked.ptt_url,Html.FROM_HTML_MODE_COMPACT))
            search_detail_url.movementMethod = LinkMovementMethod.getInstance()
            search_constraint1.visibility = View.GONE
            val params = LinearLayout.LayoutParams(search_constraint2.getLayoutParams())
            params.setMargins(0, 400, 0, 0)
            search_constraint2.setLayoutParams(params)
        }

        search_details_back.setOnClickListener {
            this.finish()
        }

    }
}
