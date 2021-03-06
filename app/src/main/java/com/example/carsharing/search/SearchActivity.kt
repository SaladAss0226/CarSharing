package com.example.carsharing.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsharing.*
import com.example.carsharing.lobby.PostAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottomsheet_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    val searchAdapter = PostAdapter()
    var currentPage = 1
    var lastPage:Int? = null

    var date:String? = null
    var departure:String? = null
    var destination:String? = null
    var type:Int? = null

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

        lateinit var itemClicked:AllpostsDetails
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rv_search_result.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        rv_search_result.adapter = searchAdapter


        PostAdapter.setToClick(object :PostAdapter.ItemClickListener {
            override fun toClick(item: AllpostsDetails) {
                itemClicked = item
                startActivity(Intent(this@SearchActivity,SearchDetailActivity::class.java))
            }
        })

        intent.extras.let {
            date = it?.getString("DATE")
            departure = it?.getString("DEPARTURE")
            destination = it?.getString("DESTINATION")
            type = it?.getInt("type")

            if (date != null) {
                date = "${date}T00:00:00+08:00"
            }
            if(type==0) type=null

            switchPage()

        }




        btn_search_next_page.setOnClickListener {
            if(currentPage>=lastPage!!) Toast.makeText(this,"已經是最後一頁了看不出來嗎",Toast.LENGTH_SHORT).show()
            else if(currentPage<lastPage!!){
                currentPage++
                switchPage()
            }
        }
        btn_search_previous_page.setOnClickListener {
            if(currentPage<=1) Toast.makeText(this,"已經是第一頁了還想怎樣:)",Toast.LENGTH_SHORT).show()
            else {
                currentPage--
                switchPage()
            }
        }




        tbar_back_search_page.setOnClickListener {
            this.finish()
        }


    }

    private fun switchPage() {
        API.apiInterface.search(
            date,
            departure,
            destination,
            type,
            30,
            currentPage
        ).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                println("response code: ${response.code()}")
                if (response.code() == 200) {
                    var body = response.body()
                    PostAdapter.list.clear()
                    PostAdapter.list.addAll(body!!.data)
                    if(PostAdapter.list.size==0) tv_no_result.visibility = View.VISIBLE
                    else tv_no_result.visibility = View.INVISIBLE

                    lastPage = body.meta.last_page
                    tv_search_current_page.text = "Page(${currentPage}/${lastPage})"
                    searchAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                Log.e("error", "${t.printStackTrace()}")
            }
        })
    }


}
