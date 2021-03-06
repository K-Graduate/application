package com.example.kgraduate.layouts


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kgraduate.R
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.databinding.ActivityMainBinding
import com.example.kgraduate.info.ReportFragment
import com.example.kgraduate.love.BreedFragment
import com.example.kgraduate.main.HomeFragment
import com.example.kgraduate.message.MessageFragment
import com.example.kgraduate.parcelout.RehomeFragment
import com.example.kgraduate.write.WritePostActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs: SharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        Log.d(TAG, "onCreate: ${prefs.getString("token", "")}")

        binding.ivBtnWrite.setOnClickListener {
            val intent = Intent(this, WritePostActivity::class.java)
            startActivity(intent)
        }


        binding.bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.loveBtn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, BreedFragment()).commit()
                }
                R.id.homeBtn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment()).commit()
                }
                R.id.messageBtn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MessageFragment()).commit()
                }
                R.id.infoBtn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ReportFragment()).commit()
                }
                R.id.parcelOutBtn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, RehomeFragment()).commit()
                }
            }
            true
        }

        // ?????? ??????
        binding.bnvMain.selectedItemId = R.id.homeBtn

    }
}