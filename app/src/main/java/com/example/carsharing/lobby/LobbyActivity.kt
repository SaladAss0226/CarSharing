package com.example.carsharing.lobby

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.carsharing.*
import com.example.carsharing.API
import com.example.carsharing.ResponseLogout
import com.example.carsharing.logIn.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.layout_bottomsheet.*
import kotlinx.android.synthetic.main.layout_bottomsheet_search.*
import kotlinx.android.synthetic.main.layout_lobby.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.view.animation.LinearInterpolator
import com.example.carsharing.search.SearchActivity


class LobbyActivity : AppCompatActivity() {
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    lateinit var bottomBehaviorSearch:BottomSheetBehavior<View>
    lateinit var bottomSheet: View
    var date: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.carsharing.R.layout.activity_lobby)

        bottomBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomBehaviorSearch = BottomSheetBehavior.from(bottom_sheet_search)
        hideBottomSheet()

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
        //搜尋
        toolbar_search.setOnClickListener {
            bottomBehaviorSearch.isHideable=false
            setSearchBottomViewVisible(bottomBehaviorSearch.state != BottomSheetBehavior.STATE_EXPANDED)
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
                        //跳回主頁
                        hideBottomSheet()
                    }
                }
            })
        }
         ObjectAnimator.ofFloat(icon_arrow_1,"alpha",0f,0.33f,0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(icon_arrow_2,"alpha",0f,0.66f,0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(icon_arrow_3,"alpha",0f,1f,0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }


        //取得日期搜尋欄
        val calendarSearch = Calendar.getInstance()
        val listenerSearch = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                calendarSearch.set(year, month, day)
                val myformat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myformat, Locale.TAIWAN)
                tv_departure_date.setText(sdf.format(calendarSearch.time))
                date = tv_departure_date.text.toString()
                println("=======date = $date")
            }
        }
        tv_departure_date.setOnClickListener{
            DatePickerDialog(this,
                listenerSearch,
                calendarSearch.get(Calendar.YEAR),
                calendarSearch.get(Calendar.MONTH),
                calendarSearch.get(Calendar.DAY_OF_MONTH)).show()
        }
        //站內搜尋
        btn_inside_post.setOnClickListener {
            val intent = SearchActivity.getIntent(this, date,et_departure_station.text.toString(),et_destination.text.toString())
//            startActivity(Intent(this,SearchActivity::class.java))
            startActivity(intent)
            bottomBehaviorSearch.isHideable=false
            setSearchBottomViewVisible(bottomBehaviorSearch.state != BottomSheetBehavior.STATE_EXPANDED)

        }
        //站外搜尋
        btn_outside_post.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        //全站搜尋
        btn_all_post.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }





    }
    fun  hideBottomSheet(){
        bottomBehavior.isHideable=true
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomBehaviorSearch.isHideable=true
        bottomBehaviorSearch.state = BottomSheetBehavior.STATE_HIDDEN

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
    private fun setSearchBottomViewVisible(showFlag: Boolean){
        if (showFlag)
            bottomBehaviorSearch.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomBehaviorSearch.state = BottomSheetBehavior.STATE_COLLAPSED

    }
    fun showArrow(){

    }
}
