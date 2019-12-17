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
import androidx.recyclerview.widget.LinearLayoutManager
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
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.R.string.no
import android.R.attr.name
import android.graphics.Rect
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MotionEvent


class LobbyActivity : AppCompatActivity() {
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    lateinit var bottomBehaviorSearch:BottomSheetBehavior<View>
//    lateinit var bottomSheet: View
     var date: String? = null
    private val postAdapter = PostAdapter()
    private var allpostsList : MutableList<AllpostsDetails> = arrayListOf()
    private var current_page: Int? = 1
    private var last_page: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)


        bottomBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomBehaviorSearch = BottomSheetBehavior.from(bottom_sheet_search)
        hideBottomSheet()
        rv_allposts.layoutManager = LinearLayoutManager(this)
        rv_allposts.adapter = postAdapter
        showPosts(current_page!!)

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
            layout_lobby.isFocusableInTouchMode = false  //???
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
                Toast.makeText(this, "欄位請勿空白", Toast.LENGTH_SHORT).show()
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
                            //跳回主頁並刷新成第一頁
                            hideBottomSheet()
                            current_page = 1
                            showPosts(current_page!!)
                        }
                    }
                })
            }
        }

        //回上頁
        btn_previous_page.setOnClickListener {
            if (current_page != 1) {
                val page = current_page!! - 1
                showPosts(page)
                rv_allposts.smoothScrollToPosition(1)
            }else{
                Toast.makeText(this, "沒有上一頁喔", Toast.LENGTH_SHORT).show()
            }
        }
        //跳下頁
        btn_next_page.setOnClickListener {
            if (current_page != last_page){
                val page = current_page!! + 1
                showPosts(page)
                rv_allposts.smoothScrollToPosition(1)
            }else{
                Toast.makeText(this, "沒有下一頁喔", Toast.LENGTH_SHORT).show()
            }
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
        //設定搜尋日期
        tv_departure_date.setOnClickListener{
            DatePickerDialog(this,
                listenerSearch,
                calendarSearch.get(Calendar.YEAR),
                calendarSearch.get(Calendar.MONTH),
                calendarSearch.get(Calendar.DAY_OF_MONTH)).show()
        }
        //站內搜尋
        btn_inside_post.setOnClickListener {
            val intent = SearchActivity.getIntent(this, date,et_departure_station.text.toString(),et_destination.text.toString(),1)
            startActivity(intent)
            bottomBehaviorSearch.isHideable=false
            setSearchBottomViewVisible(bottomBehaviorSearch.state != BottomSheetBehavior.STATE_EXPANDED)

        }
        //站外搜尋
        btn_outside_post.setOnClickListener {
            val intent = SearchActivity.getIntent(this, date,et_departure_station.text.toString(),et_destination.text.toString(),2)
            startActivity(intent)
            bottomBehaviorSearch.isHideable=false
            setSearchBottomViewVisible(bottomBehaviorSearch.state != BottomSheetBehavior.STATE_EXPANDED)
        }
        //全站搜尋
        btn_all_post.setOnClickListener {
            val intent = SearchActivity.getIntent(this, date,et_departure_station.text.toString(),et_destination.text.toString(),0)
            startActivity(intent)
            bottomBehaviorSearch.isHideable=false
            setSearchBottomViewVisible(bottomBehaviorSearch.state != BottomSheetBehavior.STATE_EXPANDED)
        }

        //bottomsheet狀態，外部是否可被點擊
        bottomBehaviorSearch.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    rv_allposts.isClickable = false
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    rv_allposts.isClickable = true
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


    }
    //點擊外部時，收起
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.getAction() == MotionEvent.ACTION_DOWN){
            if (bottomBehaviorSearch.getState()==BottomSheetBehavior.STATE_EXPANDED) {
                val outRect = Rect()
                bottom_sheet_search.getGlobalVisibleRect(outRect)
                if(!outRect.contains(event.getRawX().toInt(), event.getRawY().toInt())){
                    bottomBehaviorSearch.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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


    //呈現全部文章
    fun showPosts(page: Int){
        API.apiInterface.getAll(page).enqueue(object: Callback<ResponseAllposts>{
            override fun onFailure(call: Call<ResponseAllposts>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResponseAllposts>, response: Response<ResponseAllposts>) {
                if (response.code()==200){
                    val responsebody = response.body()
                    //先紀錄目前頁數和最尾頁數
                    current_page = responsebody!!.meta.current_page
                    last_page = responsebody.meta.last_page
                    println("=======current_page=$current_page")
                    println("=======last_page=$last_page")
                    runOnUiThread{
                        pageTextView.text="Page ($current_page / $last_page)"
                    }
                    val dataList = responsebody.data
                    allpostsList.clear()
                    allpostsList.addAll(dataList)
                    postAdapter.update(allpostsList)
                    postAdapter.setToClick(object : PostAdapter.ItemClickListener{
                        override fun toClick(item: AllpostsDetails) {
                            val intent = Intent(this@LobbyActivity, DetailActivity::class.java)
                            intent.putExtra("type", item.type)
                            intent.putExtra("departure", item.departure)
                            intent.putExtra("destination", item.destination)
                            intent.putExtra("subject", item.subject)
                            intent.putExtra("date", item.departure_date)
                            intent.putExtra("seat", item.seat)
                            intent.putExtra("description", item.description)
                            intent.putExtra("id", item.id)
                            intent.putExtra("url", item.ptt_url)
                            startActivityForResult(intent, 1)
                        }
                    })
                }
            }
        })
    }
}
