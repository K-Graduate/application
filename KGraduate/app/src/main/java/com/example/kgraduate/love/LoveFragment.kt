package com.example.kgraduate.love

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kgraduate.databinding.FragmentLoveBinding

class LoveFragment : Fragment() {
    lateinit var binding : FragmentLoveBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoveBinding.inflate(inflater,container,false)
        return binding.root
    }
}