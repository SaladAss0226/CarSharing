package com.example.carsharing.lobby

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsharing.*
import com.example.carsharing.logIn.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.layout_bottomsheet.*
import kotlinx.android.synthetic.main.layout_lobby.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LobbyActivity : AppCompatActivity() {
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    lateinit var bottomSheet: View
    private var date: String? = null
    private val postAdapter = PostAdapter()
    private var allpostsList : MutableList<AllpostsDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)


        bottomBehavior = BottomSheetBehavior.from(bottom_sheet)
        hideBottomSheet()
        rv_allposts.layoutManager = LinearLayoutManager(this)
        rv_allposts.adapter = postAdapter
        showPosts()

        //登出
        toolbar_back.setOnClickListener{
            API.apiInterface.logout().enqueue(object : Callback<ResponseLogout>{
                override fun onFailure(call: Call<ResponseLogout>, t: Throwable) {
                    println("===========$t")
                }
                override fun onResponse(call: Call<ResponseLogout>, response: Response<ResponseLogout>) {
                    if (response.code() == 200){
                        val intent = Intent(this@LobbyActivity, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@LobbyActivity, "登出", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            //發案子
            toolbar_add.setOnClickListener{

            }

        }

        //刊登
        toolbar_add.setOnClickListener{
            bottomBehavior.isHideable=false
            setBottomViewVisible(bottomBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
        }

        //刊登頁返回
        sheet_back.setOnClickListener{
            hideBottomSheet()
        }
        //取得時間
        val calendar = Calendar.getInstance()
        val listener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                calendar.set(year, month, day)
                val myformat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myformat, Locale.TAIWAN)
                ed_date.text = sdf.format(calendar.time)
                date = ed_date.text.toString()
                println("=======date = $date")
            }
        }
        ed_date.setOnClickListener{
            DatePickerDialog(this,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        //發送刊登
        sheet_finish.setOnClickListener {
            if(ed_departure.text == null || ed_date.text == null || ed_description.text == null ||
                ed_destination.text == null || ed_seat.text == null || ed_subject.text == null){
                Toast.makeText(this@LobbyActivity, "欄位請勿空白", Toast.LENGTH_SHORT).show()
            }else{
                API.apiInterface.post(RequestPost(ed_departure.text.toString(), "${date}T00:00:00+08:00",
                    ed_description.text.toString(), ed_destination.text.toString(), ed_seat.text.toString(),
                    ed_subject.text.toString())).enqueue(object: Callback<ResponsePost>{
                    override fun onFailure(call: Call<ResponsePost>, t: Throwable) {
                        println("========$t")
                    }
                    override fun onResponse(call: Call<ResponsePost>, response: Response<ResponsePost>) {
                        if(response.code()==200){
                            Toast.makeText(this@LobbyActivity, "刊登成功！",Toast.LENGTH_SHORT).show()
                            //送出成功後清空
                            ed_departure.setText("")
                            ed_date.setText("")
                            ed_subject.setText("")
                            ed_seat.setText("")
                            ed_destination.setText("")
                            ed_description.setText("")
                            //跳回主頁並刷新
                            hideBottomSheet()
                            showPosts()
                        }
                    }
                })
            }
        }




    }
    fun  hideBottomSheet(){
        bottomBehavior.isHideable=true
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }
    fun showBottomSheet(v: View) {
        bottomBehavior.isHideable=false
        setBottomViewVisible(bottomBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
    }

    private fun setBottomViewVisible(showFlag: Boolean) {
        if (showFlag)
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    //呈現全部文章
    fun showPosts(){
        API.apiInterface.getAll().enqueue(object: Callback<ResponseAllposts>{
            override fun onFailure(call: Call<ResponseAllposts>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResponseAllposts>, response: Response<ResponseAllposts>) {
                if (response.code()==200){
                    val responsebody = response.body()
                    val dataList = responsebody!!.data
                    allpostsList.addAll(dataList)
                    postAdapter.update(allpostsList)
                    postAdapter.setToClick(object : PostAdapter.ItemClickListener{
                        override fun toClick(item: AllpostsDetails) {
                            val intent = Intent(this@LobbyActivity, DetailActivity::class.java)
                            intent.putExtra("departure", item.departure)
                            intent.putExtra("destination", item.destination)
                            intent.putExtra("subject", item.subject)
                            intent.putExtra("date", item.departure_date)
                            intent.putExtra("seat", item.seat)
                            intent.putExtra("description", item.description)
                            intent.putExtra("id", item.id)
                            startActivityForResult(intent, 1)

                        }
                    })
                }
            }
        })
    }
}
