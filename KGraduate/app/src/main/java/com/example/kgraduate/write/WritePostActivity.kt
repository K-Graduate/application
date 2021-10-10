package com.example.kgraduate.write

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kgraduate.databinding.ActivityWritePostBinding
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import kotlinx.android.synthetic.main.activity_write_post.*
import java.lang.Exception

class WritePostActivity : AppCompatActivity() {
    lateinit var binding : ActivityWritePostBinding
    private val uriList = mutableListOf<Uri>()
    lateinit var mAdapter : MultiImageAdapter

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val data = result.data
            if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{   // 이미지를 하나라도 선택한 경우
                if(data.clipData == null){     // 이미지를 하나만 선택한 경우
                    val imageUri = data.data!!
                    uriList.add(imageUri)
                }
                else{      // 이미지를 여러장 선택한 경우
                    Log.d(TAG, "many pictures: ")
                    val clipData = data.clipData

                    if(clipData!!.itemCount > 10){   // 선택한 이미지가 11장 이상인 경우
                        Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    }
                    else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, "multiple choice")

                        for(i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            try {
                                uriList.add(imageUri)
                            } catch (e : Exception) {
                                Log.e(TAG, "File select error: ", e)
                            }
                        }
                        binding.tvPicture.text = "${uriList.size}"
                        mAdapter.mData = uriList
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = MultiImageAdapter(this)
        rv_picture.adapter = mAdapter   // 리사이클러뷰에 어댑터 세팅
        rv_picture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    // 리사이클러뷰 수평 스크롤 적용

        binding.tvClose.setOnClickListener {
            finish()
        }

        binding.ivCamera.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            getContent.launch(intent)
        }
    }
}