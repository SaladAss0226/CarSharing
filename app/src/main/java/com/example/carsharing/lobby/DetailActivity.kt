package com.example.carsharing.lobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.carsharing.R
import kotlinx.android.synthetic.main.activity_details.*
import android.widget.LinearLayout
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class DetailActivity : AppCompatActivity() {
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val type = intent.getIntExtra("type", 1)
        val departure = intent.getStringExtra("departure")
        val destination = intent.getStringExtra("destination")
        val subject = intent.getStringExtra("subject")
        val date = intent.getStringExtra("date")
        val seat = intent.getIntExtra("seat", 0)
        val description = intent.getStringExtra("description")
        val url = intent.getStringExtra("url")
        id = intent.getIntExtra("id", 0)   //紀錄行程，報名api需要

        details_departure.text = departure
        detail_destination.text = destination
        detail_subject.text = subject
        detail_date.text = date
        if (seat != 0){
            detail_seat.text = "剩餘 ${seat} 個座位"
        }
        if (type == 2){
            constraint1.visibility = View.GONE
            val params = LinearLayout.LayoutParams(constraint2.getLayoutParams())
            params.setMargins(0, 400, 0, 0)
            constraint2.setLayoutParams(params)
        }
        detail_describe.text = description
        detail_url.text = url

        //返回鍵
        details_back.setOnClickListener {
            finish()
        }
    }
}
