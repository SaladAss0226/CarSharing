package com.example.carsharing.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsharing.API
import com.example.carsharing.R
import com.example.carsharing.ResponseSearch
import com.example.carsharing.SearchDetails
import com.example.carsharing.search.SearchAdapter.Companion.unAssignList
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottomsheet_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    val searchAdapter = SearchAdapter()

    companion object {
        fun getIntent(
            context: Context,
            date: String?,
            departureStation: String?,
            destinationStation: String?,
            type:Int
        ): Intent {
            val intent = Intent()
            intent.putExtra("DATE", date)
            intent.putExtra("DEPARTURE", departureStation)
            intent.putExtra("DESTINATION", destinationStation)
            intent.putExtra("type",type)
            intent.setClass(context, SearchActivity::class.java)
            return intent
        }

        lateinit var itemClicked:SearchDetails
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        SearchAdapter.setToClick(object :SearchAdapter.mItemClickListener {
            override fun toClick(items: SearchDetails) {
                itemClicked = items
                startActivity(Intent(this@SearchActivity,SearchDetailActivity::class.java))
            }
        })

        initResults()

        intent.extras.let {
            var date = it?.getString("DATE")
            val departure = it?.getString("DEPARTURE")
            val destination = it?.getString("DESTINATION")
            var type = it?.getInt("type")

            if (date != null) {
                date = "${date}T00:00:00+08:00"
            }
            if(type==0) type=null

            //呼叫api
        API.apiInterface.search(
            date,
            departure,
            destination,
            type,
            15).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                println("response code: ${response.code()}")
                if(response.code()==200){
                    var body = response.body()
                    unAssignList.clear()
                    unAssignList.addAll(body!!.data)
                    searchAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                Log.e("error","${t.printStackTrace()}")
            }
        })

        }




        tbar_back_search_page.setOnClickListener {
            this.finish()
        }


    }

    private fun initResults() {
        rv_search_result.layoutManager = LinearLayoutManager(
            this@SearchActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_search_result.adapter = searchAdapter
    }


}
