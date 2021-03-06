package com.example.carsharing.logIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.carsharing.API
import com.example.carsharing.R
import com.example.carsharing.RequestSignup
import com.example.carsharing.ResponseSignup
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //註冊按鈕
        btn_sign_up.setOnClickListener {
            if (et_account_sign_up.text.isNullOrEmpty() || et_password_sign_up.text.isNullOrEmpty() || et_re_password.text.isNullOrEmpty()){
                Toast.makeText(this,"請設定帳號或密碼",Toast.LENGTH_SHORT).show()
            }else if(!et_account_sign_up.text.contains("@")){
                Toast.makeText(this,"e-mail形式不符，請再檢查一次",Toast.LENGTH_SHORT).show()
            }else{
                //檢查完條件，註冊帳號
                signup_API(et_account_sign_up.text.toString(), et_password_sign_up.text.toString(), et_re_password.text.toString())
            }
        }
    }

    //註冊的API
    private fun signup_API(account: String, password: String, repassword: String){
        API.apiInterface.signUp(RequestSignup(account, password, repassword)).enqueue(object : Callback<ResponseSignup>{
            override fun onResponse(call: Call<ResponseSignup>, response: Response<ResponseSignup>){
                if(response.code()==200){
                    Toast.makeText(this@SignUpActivity,"註冊成功",Toast.LENGTH_SHORT).show()
                    this@SignUpActivity.finish()
                }
            }
            override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                Log.e("*****error","${t.printStackTrace()}")
                Toast.makeText(this@SignUpActivity,"註冊失敗，請再檢查一次",Toast.LENGTH_SHORT).show()
            }
        })
    }

}
