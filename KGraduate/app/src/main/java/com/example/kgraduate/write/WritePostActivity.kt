package com.example.kgraduate.write

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgraduate.databinding.ActivityWritePostBinding
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.posts.PostResponse
import com.example.kgraduate.posts.PostService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception

class WritePostActivity : AppCompatActivity(), onRemoveClick{
    lateinit var binding : ActivityWritePostBinding
    private val uriList = mutableListOf<Uri>()
    lateinit var mAdapter : MultiImageAdapter

    lateinit var retrofit : Retrofit
    lateinit var postService: PostService
    var body : PostResponse? = null

    lateinit var prefs : SharedPreferences

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

                    if(clipData!!.itemCount > 5){   // 선택한 이미지가 11장 이상인 경우
                        Toast.makeText(this, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    }
                    else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, "multiple choice")

                        for(i in 0 until clipData.itemCount) {
                            val imageUri = clipData.getItemAt(i).uri
                            try {
                                uriList.add(imageUri)
                                val file = File(imageUri.path!!)
                                var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"),file)
                                var filePart : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file","1",requestBody)
                                
                                postService.uploadImg(/*prefs.getString("token","")!!,*/ filePart).enqueue(object : Callback<PostResponse> {
                                    override fun onResponse(
                                        call: Call<PostResponse>,
                                        response: Response<PostResponse>
                                    ) {
                                        Log.d(TAG, "onResponse: 업로드 성공! file_id : ${response.body()?.message}")
                                    }

                                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                        Log.d(TAG, "onFailure: 업로드 실패ㅠ")
                                    }

                                })
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

        retrofit = Retrofit.Builder()
            .baseUrl("http://18.223.182.55:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postService = retrofit.create(
            PostService::class.java)

        prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        mAdapter = MultiImageAdapter(this,this)
        binding.rvPicture.adapter = mAdapter   // 리사이클러뷰에 어댑터 세팅
        binding.rvPicture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    // 리사이클러뷰 수평 스크롤 적용

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

        binding.tvPost.setOnClickListener {
            postService.registerPost(/*prefs.getString("token","")!!,*/ "분양", "hehtrhrth", "reghregergerg", "2021/10/29-14-00-05", "test", "1").enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    body = response.body()
                    when(body?.code) {
                        "200"-> {
                            Log.d(TAG, "onResponse: 업로드 완료!")
                        }
                        else -> {
                            Log.d(TAG, "onResponse: ${body?.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: 업로드 실패ㅠ")
                }

            })
        }
    }

    override fun onRemoveClicked(value: Int) {
        binding.tvPicture.text = value.toString()
    }
}