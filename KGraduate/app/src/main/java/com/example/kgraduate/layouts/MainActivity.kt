package com.example.kgraduate.layouts


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kgraduate.Layouts.LoginActivity.Companion.TAG
import com.example.kgraduate.login.Login
import com.example.kgraduate.login.LoginService
import com.example.kgraduate.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs : SharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        Log.d(TAG, "onCreate: ${prefs.getString("token","")}")
    }
}