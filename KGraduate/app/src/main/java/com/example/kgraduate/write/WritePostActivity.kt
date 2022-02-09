package com.example.kgraduate.write

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgraduate.R
import com.example.kgraduate.databinding.ActivityWritePostBinding
import com.example.kgraduate.login.LoginActivity.Companion.ServerUrl
import com.example.kgraduate.repository.dto.response.PostResponse
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.posts.PostService
import com.example.kgraduate.repository.dto.response.ImageResponse
import com.example.kgraduate.repository.entity.Post
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WritePostActivity : AppCompatActivity(), onRemoveClick {
    lateinit var binding: ActivityWritePostBinding
    private val uriList = mutableListOf<Uri>()
    private var idList = arrayListOf<String>()
    lateinit var mAdapter: MultiImageAdapter

    lateinit var retrofit: Retrofit
    lateinit var postService: PostService
    var body: PostResponse? = null
    lateinit var post_type : String

    lateinit var prefs: SharedPreferences

    // 갤러리에서 이미지 선택 후 받아오는 결과값
    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { it ->
            it.forEach {
                uriList.add(it)

                var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                var c: Cursor =
                    contentResolver.query(it, proj, null, null, null)!!
                var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                c.moveToFirst()

                var result = c.getString(index)
                Log.d(TAG, "absolute path : $result")

                // 선택한 이미지 File형으로 형변환
                val file = File(result)

                val requestBody: RequestBody =
                    RequestBody.create(MediaType.parse("image/jpeg"), file)
                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("img", "1.jpg", requestBody)

                // 서버에 업로드 요청
                postService.uploadImg(/*prefs.getString("token","")!!,*/ filePart)
                    .enqueue(object : Callback<ImageResponse> {
                        override fun onResponse(
                            call: Call<ImageResponse>,
                            response: Response<ImageResponse>
                        ) {
                            // response로 file_id를 받아옴
                            Log.d(
                                TAG,
                                "onResponse: 사진 업로드 성공! file_id : ${response.body()?.file_id}"
                            )
                            idList.add(response.body()?.file_id!!)
                        }

                        override fun onFailure(
                            call: Call<ImageResponse>,
                            t: Throwable
                        ) {
                            Log.d(TAG, "onFailure: ${t.message}")
                        }
                    })
            }
            binding.tvPicture.text = "${uriList.size}"
            mAdapter.mData = uriList
            mAdapter.notifyDataSetChanged()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이미지 가져오는 권한 설정
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }

        retrofit = Retrofit.Builder()
            .baseUrl(ServerUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postService = retrofit.create(
            PostService::class.java
        )

        prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        // 어댑터 설정
        mAdapter = MultiImageAdapter(this, this)
        binding.rvPicture.adapter = mAdapter   // 리사이클러뷰에 어댑터 세팅
        binding.rvPicture.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    // 리사이클러뷰 수평 스크롤 적용

        // 창 닫기
        binding.tvClose.setOnClickListener {
            finish()
        }

        // Spinner를 위한 ArrayAdapter 설정
        val types = resources.getStringArray(R.array.post_type)
        binding.spType.adapter = ArrayAdapter(this,R.layout.spinner_item,types)
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val type = binding.spType.getItemAtPosition(p2)
                post_type = when(type) {
                    "분양" -> "rehome"
                    "교배" -> "breed"
                    else -> "inform"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // 갤러리에서 이미지 가져오기
        binding.ivCamera.setOnClickListener {
            launcher.launch("image/*")
        }

        // 게시글 업로드 버튼
        binding.tvPost.setOnClickListener {
            val jsonObject = JsonObject()

            jsonObject.addProperty("type", post_type)
            jsonObject.addProperty("title", binding.etTitle.text.toString())
            jsonObject.addProperty("content",binding.etContent.text.toString())
            jsonObject.addProperty("creator_name","tester")
            jsonObject.addProperty("creator_id", "11")

            val long_now = System.currentTimeMillis()

            // 현재 시간을 Date 타입으로 변환
            val t_date = Date(long_now)

            // 날짜, 시간을 가져오고 싶은 형태 선언
            val t_dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale("ko", "KR"))
            val str_date = t_dateFormat.format(t_date)

            jsonObject.addProperty("creator_time",str_date)
            //jsonObject.addProperty("comments","0")
            //jsonObject.addProperty("like","0")

            val jsonArray = JsonArray()
            idList.forEach {
                jsonArray.add(it)
            }

            jsonObject.add("file_id",jsonArray)
            Log.d(TAG, "게시물 post : $jsonObject")

            postService.registerPost(/*"Bearer " + prefs.getString("token","")!!,*/
                jsonObject
            ).enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    body = response.body()
                    when (body?.code) {
                        "200" -> {
                            Log.d(TAG, "onResponse: 업로드 완료!")
                            finish()
                        }
                        else -> {
                            Log.d(TAG, "code: ${body?.code}")
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

    // 사진 삭제 버튼 클릭 시 사진 갯수 업데이트
    override fun onRemoveClicked(value: Int) {
        binding.tvPicture.text = value.toString()
    }
}