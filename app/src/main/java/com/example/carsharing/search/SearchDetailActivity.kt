package com.example.carsharing.search

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.example.carsharing.R
import com.example.carsharing.search.SearchActivity.Companion.itemClicked
import com.example.carsharing.search.SearchAdapter.Companion.unAssignList
import kotlinx.android.synthetic.main.activity_search_detail.*

class SearchDetailActivity : AppCompatActivity() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        details_departure.text = itemClicked.departure
        detail_destination.text = itemClicked.destination
        detail_subject.text = itemClicked.subject
        detail_date.text = itemClicked.departure_date
        detail_seat.text = itemClicked.seat.toString()
        detail_describe.text = itemClicked.description
        detail_url.movementMethod = LinkMovementMethod.getInstance()

        if(itemClicked.ptt_url==null) detail_url.text = ""
        else if(itemClicked.ptt_url!=null){
//            detail_url.setText(Html.fromHtml(itemClicked.ptt_url,Html.FROM_HTML_MODE_COMPACT))
            if (Build.VERSION.SDK_INT >= 24) {
                detail_url.text = Html.fromHtml(itemClicked.ptt_url,Html.FROM_HTML_MODE_COMPACT)
            }
            else{
                detail_url.text = Html.fromHtml(itemClicked.ptt_url)
            }
        }



        details_back.setOnClickListener {
            this.finish()
        }

    }
}
