package com.example.kgraduate.parcelout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kgraduate.databinding.FragmentRehomeBinding

class RehomeFragment : Fragment() {
    lateinit var binding: FragmentRehomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRehomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}