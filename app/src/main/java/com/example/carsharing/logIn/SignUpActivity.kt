package com.example.carsharing.logIn

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

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //註冊按鈕
        btn_sign_up.setOnClickListener {
            if(et_re_password.text.toString().equals(et_password_sign_up.text.toString()) && et_account_sign_up.text.contains("@")){
                API.apiInterface.signUp(RequestSignup(
                    et_account_sign_up.text.toString(),
                    et_password_sign_up.text.toString(),
                    et_re_password.text.toString()
                )).enqueue(object : Callback<ResponseSignup>{
                    override fun onResponse(
                        call: Call<ResponseSignup>,
                        response: Response<ResponseSignup>){
                        println("response: ${response.code()}")

                        if(response.code()==200){
                            Toast.makeText(this@SignUpActivity,"註冊成功",Toast.LENGTH_SHORT).show()
                            this@SignUpActivity.finish()
                        }
                        else Toast.makeText(this@SignUpActivity,"發生錯誤，註冊失敗",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                        Log.e("*****error","${t.printStackTrace()}")
                    }
                })

            }
            else if(et_re_password.text!==et_password_sign_up.text) Toast.makeText(this,"密碼輸入不相同，請再檢查一次",Toast.LENGTH_SHORT).show()
            else Toast.makeText(this,"e-mail形式不符，請再檢查一次",Toast.LENGTH_SHORT).show()

        }



    }
}
