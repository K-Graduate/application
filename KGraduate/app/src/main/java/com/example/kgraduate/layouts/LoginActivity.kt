package com.example.kgraduate.layouts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kgraduate.login.Login
import com.example.kgraduate.login.LoginService
import com.example.kgraduate.R
import kotlinx.android.synthetic.main.activity_login.signIn
import kotlinx.android.synthetic.main.activity_login.logInId
import kotlinx.android.synthetic.main.activity_login.logInPassword
import kotlinx.android.synthetic.main.activity_login.tv_register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "TAG_GRADUATE"
    }

    var login: Login? = null
    lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("Prefs",Context.MODE_PRIVATE)


        var retrofit = Retrofit.Builder()
            .baseUrl("http://18.223.182.55:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var loginService: LoginService = retrofit.create(
                LoginService::class.java)

        // 회원가입 하러가기
        tv_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        signIn.setOnClickListener {
            val id = logInId.text.toString()
            val pwd = logInPassword.text.toString()

            if(id=="") {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(pwd=="") {
                Toast.makeText(this,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                loginService.requestLogin(id, pwd).enqueue(object : Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        login = response.body()
                        Log.d(TAG, "onResponse Code: ${login?.code}")
                        Log.d(TAG, "onResponse Token: ${login?.token}")

                        when(login?.code) {
                            "200" -> {
                                val editor = prefs.edit()
                                editor.putString("token", login?.token)
                                editor.commit()

                                startActivity(intent)
                            }
                            else -> {
                                Toast.makeText(applicationContext,"아이디 또는 비밀번호가 잘못되었습니다!",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })

            }
        }
    }
}