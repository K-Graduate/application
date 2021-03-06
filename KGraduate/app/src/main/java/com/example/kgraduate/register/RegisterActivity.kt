package com.example.kgraduate.register

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.databinding.ActivityRegisterBinding
import com.example.kgraduate.login.LoginActivity.Companion.ServerUrl
import com.example.kgraduate.repository.entity.Register
import com.example.kgraduate.repository.entity.Repetition
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    var register: Register? = null
    var repetition: Repetition? = null
    var repetitionCheck: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로 가기
        binding.ivBack.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(ServerUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService: RegisterService = retrofit.create(
            RegisterService::class.java
        )

        // 아이디 입력 변화시 중복 확인 여부 초기화
        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                repetitionCheck = false
            }
        })

        // 비밀번호 조건 확인 코드
        binding.etPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val pwd = binding.etPwd.text.toString()

                if (pwd.length in 8..15) {
                    if (isPasswordFormat(pwd)) {

                        binding.tvPwdRes.text = "비밀번호가 조건에 맞습니다!"
                        binding.tvPwdRes.setTextColor(Color.BLUE)
                    } else {
                        binding.tvPwdRes.text = "비밀번호가 조건에 맞지 않습니다!"
                        binding.tvPwdRes.setTextColor(Color.RED)
                    }
                } else {
                    binding.tvPwdRes.text = "비밀번호가 조건에 맞지 않습니다!"
                    binding.tvPwdRes.setTextColor(Color.RED)
                }
            }
        })

        // 비밀번호 확인 코드
        binding.etPwdCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val pwd = binding.etPwd.text.toString()
                val check_pwd = binding.etPwdCheck.text.toString()
                if (check_pwd.isNotEmpty()) {
                    if (pwd.length != check_pwd.length) {
                        binding.tvPwdCheckRes.text = "비밀번호가 일치하지 않습니다!"
                        binding.tvPwdCheckRes.setTextColor(Color.RED)
                    } else {
                        if (pwd == check_pwd) {
                            binding.tvPwdCheckRes.text = "비밀번호가 일치합니다!"
                            binding.tvPwdCheckRes.setTextColor(Color.BLUE)
                        }
                    }
                } else {
                    binding.tvPwdCheckRes.text = "비밀번호가 일치하지 않습니다!"
                    binding.tvPwdCheckRes.setTextColor(Color.RED)
                }
            }
        })

        // 아이디 중복 확인 코드
        binding.btnCheck.setOnClickListener {
            val id = binding.etId.text.toString()


            // 이메일 형식 확인
            if (id.contains("@") and id.contains(".com")) {
                Log.d(TAG, "id : $id")
                registerService.checkRepetition(id).enqueue(object : Callback<Repetition> {
                    override fun onResponse(
                        call: Call<Repetition>,
                        response: Response<Repetition>
                    ) {
                        repetition = response.body()
                        Log.d(TAG, "onResponse: $repetition")
                        when (repetition?.code) {
                            200 -> {
                                repetitionCheck = true
                                binding.tvIdRes.text = "해당 아이디는 사용할 수 있습니다!"
                                binding.tvIdRes.setTextColor(Color.BLUE)
                                Toast.makeText(
                                    applicationContext,
                                    "해당 아이디는 사용할 수 있습니다!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                binding.tvIdRes.text = "해당 아이디는 이미 존재합니다!"
                                binding.tvIdRes.setTextColor(Color.RED)
                                Toast.makeText(
                                    applicationContext,
                                    "해당 아이디는 이미 존재합니다!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Repetition>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
            } else {
                binding.tvIdRes.text = "이메일 형식이 잘못됐습니다!"
                binding.tvIdRes.setTextColor(Color.RED)
            }
        }

        // 회원 가입 코드
        binding.btnRegister.setOnClickListener {
            when (repetitionCheck) {
                true -> {
                    val id = binding.etId.text.toString()
                    val pwd = binding.etPwd.text.toString()
                    val name = binding.etName.text.toString()
                    registerService.requestRegister(id, pwd, name)
                        .enqueue(object : Callback<Register> {
                            override fun onResponse(
                                call: Call<Register>,
                                response: Response<Register>
                            ) {
                                register = response.body()
                                when (register?.code) {
                                    "200" -> {
                                        binding.etPwd.setText("")
                                        binding.etPwdCheck.setText("")
                                        binding.etId.setText("")
                                        binding.etName.setText("")
                                        binding.tvIdRes.text = ""

                                        Toast.makeText(
                                            applicationContext,
                                            "회원가입이 정상적으로 완료됐습니다!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                                    else -> {
                                        Toast.makeText(
                                            applicationContext,
                                            "회원가입 도중 오류가 발생했습니다!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Register>, t: Throwable) {
                                Log.d(TAG, "onFailure: ${t.message}")
                            }
                        })
                }
                false -> {
                    Log.d(TAG, "회원가입 버튼 클릭")
                    Toast.makeText(applicationContext, "아이디 중복 확인을 해주십시오!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // 비밀번호 정규식
    fun isPasswordFormat(password: String): Boolean {
        return password.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$".toRegex())
    }
}


