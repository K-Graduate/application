package com.example.kgraduate.Layouts


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kgraduate.Login.Login
import com.example.kgraduate.Login.LoginService
import com.example.kgraduate.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG = "TAG_GRADUATE"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs : SharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        Log.d(TAG, "onCreate: ${prefs.getString("token","")}")
    }
}