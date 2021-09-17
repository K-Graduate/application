package com.example.kgraduate.Layouts

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kgraduate.Login.Login
import com.example.kgraduate.Login.LoginService
import com.example.kgraduate.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    val TAG = "TAG_GRADUATE"
    var login: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://18.223.182.55:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var loginService: LoginService = retrofit.create(
                LoginService::class.java)

        cv_login.setOnClickListener {
            val id = et_hakbun.text.toString()
            val pwd = et_pwd.text.toString()

            if(id=="") {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(pwd=="") {
                Toast.makeText(this,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()
            }
            else {
//                GlobalScope.launch {
//                    val url =
//                }
                loginService.requestLogin(id, pwd).enqueue(object : Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        login = response.body()
                        Log.d(TAG, "onResponse Code: ${login?.code}")
                        Log.d(TAG, "onResponse Token: ${login?.token}")
                    }
                })
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}