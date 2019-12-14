package com.example.carsharing.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carsharing.R
import com.example.carsharing.search.SearchActivity.Companion.itemClicked
import com.example.carsharing.search.SearchAdapter.Companion.unAssignList
import kotlinx.android.synthetic.main.activity_search_detail.*

class SearchDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        details_departure.text = itemClicked.departure
        detail_destination.text = itemClicked.destination
        detail_subject.text = itemClicked.subject
        detail_date.text = itemClicked.departure_date
        detail_seat.text = itemClicked.seat.toString()
        detail_describe.text = itemClicked.description

        details_back.setOnClickListener {
            this.finish()
        }

    }
}
