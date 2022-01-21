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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgraduate.databinding.ActivityWritePostBinding
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
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception

class WritePostActivity : AppCompatActivity(), onRemoveClick {
    lateinit var binding: ActivityWritePostBinding
    private val uriList = mutableListOf<Uri>()
    private var idList = arrayListOf<String>()
    lateinit var mAdapter: MultiImageAdapter

    lateinit var retrofit: Retrofit
    lateinit var postService: PostService
    var body: PostResponse? = null

    lateinit var prefs: SharedPreferences

    // 갤러리에서 이미지 선택 후 받아오는 결과값
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val data = result.data
            if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
                Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {   // 이미지를 하나라도 선택한 경우
                val clipData = data.clipData
                Log.e(TAG, "img count : ${clipData!!.itemCount + uriList.size}")
                if (clipData!!.itemCount + uriList.size > 5) {   // 선택한 이미지가 6장 이상인 경우
                    Toast.makeText(this, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                } else {   // 선택한 이미지가 1장 이상 5장 이하인 경우
                    Log.e(TAG, "multiple choice")

                    // 선택한 사진들 하나씩 리스트에 넣고 서버에 업로드
                    for (i in 0 until clipData.itemCount) {
                        // 선택한 이미지 uri
                        val imageUri = clipData.getItemAt(i).uri
                        try {
                            Log.d(TAG, "$imageUri")
                            // uri 리스트에 저장
                            uriList.add(imageUri)

                            // 선택한 이미지 절대경로 가져오기
                            var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                            var c: Cursor =
                                contentResolver.query(imageUri, proj, null, null, null)!!
                            var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            c.moveToFirst()

                            var result = c.getString(index)
                            Log.d(TAG, "absolute path : $result")

                            // 선택한 이미지 File형으로 형변환
                            val file = File(result)

                            // 여기 밑에는 일단 남겨두기!
//                                    if(!file.exists())
//                                        file.mkdirs()
//                                    val inputStream = applicationContext.contentResolver.openInputStream(imageUri)
//                                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                                    val byteArrayOutputStream = ByteArrayOutputStream()
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)

//                                    val requestBody : RequestBody = RequestBody.create(MediaType.parse("image/jpeg"),byteArrayOutputStream.toByteArray())

                            // 이미지를 part 형태로 변환
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
                        } catch (e: Exception) {
                            Log.e(TAG, "File select error: ", e)
                        }
                    }
                    // 선택한 사진 갯수 업데이트 및 어댑터 새로고침
                    binding.tvPicture.text = "${uriList.size}"
                    mAdapter.mData = uriList
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

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
            .baseUrl("http://175.123.112.88:8080")
            //.baseUrl("http://18.223.182.55:8080")
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

        // 갤러리에서 이미지 가져오기
        binding.ivCamera.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            getContent.launch(intent)
            launcher.launch("image/*")
        }

        // 게시글 업로드 버튼
        binding.tvPost.setOnClickListener {
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", "분양")
            jsonObject.addProperty("title", binding.etTitle.text.toString())
            jsonObject.addProperty("content",binding.etContent.text.toString())
            jsonObject.addProperty("creator_name","tester")
            jsonObject.addProperty("creator_id", "11")
            jsonObject.addProperty("creator_time","2022-01-17 23:49:23")
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

    class innerPost(val type: String, val title: String, val content: String, val creator_name: String, val creator_id: String, val file_id: JSONArray)
}