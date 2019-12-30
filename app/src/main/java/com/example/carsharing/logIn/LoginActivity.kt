package com.example.carsharing.logIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carsharing.API
import com.example.carsharing.R
import com.example.carsharing.RequestLogin
import com.example.carsharing.ResponseLogin
import com.example.carsharing.lobby.LobbyActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient:GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        callbackManager= CallbackManager.Factory.create()

        //測試用
        et_account_signin.setText("opp@gmail.com")
        et_password_signin.setText("oppopp")

        //前往註冊頁
        btn_sign_up.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        //一般登入按鈕
        btn_sign_in.setOnClickListener {
            if(et_account_signin.text.isNullOrEmpty() || et_password_signin.text.isNullOrEmpty()){
                Toast.makeText(this,"請輸入帳號密碼",Toast.LENGTH_SHORT).show()
            }
            else{
                login_API(et_account_signin.text.toString(), et_password_signin.text.toString())

            }
        }

        //google登入按鈕
        btn_google_login.setOnClickListener{
            google_login()
            btn_google_login.visibility = View.INVISIBLE
            btn_google_logout.visibility = View.VISIBLE
        }

        //google登出按鈕
        btn_google_logout.setOnClickListener {
            google_logOut()
            btn_google_login.visibility = View.VISIBLE
            btn_google_logout.visibility = View.INVISIBLE
        }

        //facebook登入按鈕
        btn_facebook_login.setReadPermissions("email")
        btn_facebook_login.registerCallback(callbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                val token = result!!.accessToken.token.toString()
                val userId= result.accessToken.userId.toString()
                println("=======facebook_Token=$token")
                println("=======facebook_UserId=$userId")

                //等Ray哥補上<第三方登入的api> 由後端去驗證tokem
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleLoginResult(result)
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }


    //一般登入API
    fun login_API(account: String, password: String){
        API.apiInterface.login(RequestLogin(account, password)).enqueue(object: retrofit2.Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>){
                if(response.code()==200){
                    API.token = response.body()!!.data[0].token
                    Toast.makeText(this@LoginActivity,"登入成功",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity,LobbyActivity::class.java))
                    this@LoginActivity.finish()
                }
                else Toast.makeText(this@LoginActivity,"發生錯誤，登入失敗",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
            }
        })
    }

    //用Google帳號登入
    private fun google_login(){
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleApiClient = GoogleApiClient
//            .Builder(this)
//            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//            .build()

        //申請憑證後拿到的ClientID
        val clientID = "638951539726-a9f2g4r99t6hnql9kcdlplk0krd6kcc4.apps.googleusercontent.com"

            var gso2 = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build()
            var mGoogleApiClient = GoogleApiClient
                .Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso2)
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso2)

        val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, 100)
    }

    private fun google_logOut(){
        mGoogleSignInClient.signOut()
    }

    //接收Google回傳結果
    private fun handleLoginResult(result: GoogleSignInResult){
        if (result.isSuccess){
            val account = result.signInAccount
            val getEmail = account!!.email
            val getidToken = account.idToken
            println("=======getEmail=$getEmail")
            println("=======getidToken=$getidToken")

            //等Ray哥補上<第三方登入的api> 由後端去驗證token
        }
    }

}
