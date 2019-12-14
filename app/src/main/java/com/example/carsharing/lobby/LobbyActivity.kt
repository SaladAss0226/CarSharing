package com.example.carsharing.lobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.carsharing.API
import com.example.carsharing.ResponseLogout
import com.example.carsharing.R
import com.example.carsharing.logIn.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.layout_bottomsheet.*
import kotlinx.android.synthetic.main.layout_lobby.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LobbyActivity : AppCompatActivity() {
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    lateinit var bottomSheet: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        bottomBehavior = BottomSheetBehavior.from(bottom_sheet)
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
}
