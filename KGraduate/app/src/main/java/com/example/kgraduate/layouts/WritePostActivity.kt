package com.example.kgraduate.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kgraduate.R
import com.example.kgraduate.databinding.ActivityWritePostBinding

class WritePostActivity : AppCompatActivity() {
    lateinit var binding : ActivityWritePostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}