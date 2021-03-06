package com.example.kgraduate.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import com.example.kgraduate.R
import com.example.kgraduate.databinding.ActivityLoginBinding
import com.example.kgraduate.repository.entity.Login
import com.example.kgraduate.layouts.MainActivity
import com.example.kgraduate.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "TAG_GRADUATE"
        //const val ServerUrl = "https://df4d-121-131-74-18.ngrok.io"
        //const val ServerUrl = "https://eunpyo.loca.lt"
        const val ServerUrl = "http://175.123.112.88:8080"
        //const val ServerUrl = "http://18.223.182.55:8080"
    }

    var login: Login? = null
    lateinit var prefs : SharedPreferences
    lateinit var binding : ActivityLoginBinding
    private lateinit var loginService : LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("Prefs",Context.MODE_PRIVATE)

        val retrofit = Retrofit.Builder()
            .baseUrl(ServerUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginService = retrofit.create(
                LoginService::class.java)

        // 회원가입 하러가기
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        binding.signIn.setOnClickListener {
            val id = binding.logInId.text.toString()
            val pwd = binding.logInPassword.text.toString()

            if(id=="") {
                binding.tvWarning.text = "아이디를 입력하세요!"
                binding.tvWarning.setTextColor(Color.RED)
                binding.logInId.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_fail)
                //Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(pwd=="") {
                binding.tvWarning.text = "비밀번호를 입력하세요!"
                binding.tvWarning.setTextColor(Color.RED)
                binding.logInPassword.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_fail)
                //Toast.makeText(this,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                login(id,pwd)
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
            }
        }
    }

    fun login(id:String,pwd:String) {
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

                        binding.tvWarning.text = ""
                        binding.logInId.setText("")
                        binding.logInPassword.setText("")
                        binding.logInId.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_edit)
                        binding.logInPassword.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_edit)
                        startActivity(intent)
                    }
                    else -> {
                        binding.tvWarning.text = "아이디 또는 비밀번호가 잘못되었습니다!"
                        binding.tvWarning.setTextColor(Color.RED)
                        binding.logInId.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_fail)
                        binding.logInPassword.background = AppCompatResources.getDrawable(applicationContext, R.drawable.login_fail)
                    }
                }
            }
        })
    }
}