package com.example.carsharing.lobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carsharing.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailActivity : AppCompatActivity() {
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val departure = intent.getStringExtra("departure")
        val destination = intent.getStringExtra("destination")
        val subject = intent.getStringExtra("subject")
        val date = intent.getStringExtra("date")
        val seat = intent.getIntExtra("seat", 0)
        val description = intent.getStringExtra("description")
        id = intent.getIntExtra("id", 0)

        details_departure.text = departure
        detail_destination.text = destination
        detail_subject.text = subject
        detail_date.text = date
        detail_seat.text = "剩餘 ${seat} 個座位"
        detail_describe.text = description

        //返回鍵
        details_back.setOnClickListener {
            finish()
        }
    }
}
