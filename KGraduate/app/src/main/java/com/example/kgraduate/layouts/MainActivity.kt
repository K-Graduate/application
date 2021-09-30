package com.example.kgraduate.layouts


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs : SharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        Log.d(TAG, "onCreate: ${prefs.getString("token","")}")
    }


}