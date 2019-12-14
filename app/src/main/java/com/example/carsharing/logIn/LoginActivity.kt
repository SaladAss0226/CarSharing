package com.example.carsharing.logIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.carsharing.API
import com.example.carsharing.R
import com.example.carsharing.RequestLogin
import com.example.carsharing.ResponseLogin
import com.example.carsharing.lobby.LobbyActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_account_signin.setText("12345@gmail.com")
        et_password_signin.setText("123123")

        //註冊
        btn_sign_up.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        //登入
        btn_sign_in.setOnClickListener {
            if(et_account_signin.text.length>0&&et_password_signin.text.length>0){
                API.apiInterface.login(RequestLogin(
                    et_account_signin.text.toString(),
                    et_password_signin.text.toString()
                )).enqueue(object : retrofit2.Callback<ResponseLogin> {
                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>){
                        println("******response.code()")

                        if(response.code()==200){
                            API.token = response.body()!!.data[0].token
                            Toast.makeText(this@LoginActivity,"登入成功",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity,LobbyActivity::class.java))
                            this@LoginActivity.finish()
                        }
                        else Toast.makeText(this@LoginActivity,"發生錯誤，登入失敗",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        Log.e("*****error","${t.printStackTrace()}")
                    }
                })

            }
            else Toast.makeText(this,"請輸入帳號密碼",Toast.LENGTH_SHORT).show()
        }
    }
}
