package com.example.kgraduate.Layouts

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.kgraduate.Layouts.LoginActivity.Companion.TAG
import com.example.kgraduate.R
import com.example.kgraduate.Register.Register
import com.example.kgraduate.Register.RegisterService
import com.example.kgraduate.Register.Repetition
import kotlinx.android.synthetic.main.activity_register.et_pwd
import kotlinx.android.synthetic.main.activity_register.et_id
import kotlinx.android.synthetic.main.activity_register.iv_back
import kotlinx.android.synthetic.main.activity_register.et_pwd_check
import kotlinx.android.synthetic.main.activity_register.et_name
import kotlinx.android.synthetic.main.activity_register.tv_pwd_res
import kotlinx.android.synthetic.main.activity_register.tv_pwd_check_res
import kotlinx.android.synthetic.main.activity_register.btn_check
import kotlinx.android.synthetic.main.activity_register.btn_register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    var repitition: Repetition? = null
    var register : Register? = null
    var repetitionCheck : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 뒤로 가기
        iv_back.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://18.223.182.55:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService: RegisterService = retrofit.create(
            RegisterService::class.java)

        // 아이디 입력 변화시 중복 확인 여부 초기화
        et_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                //repetitionCheck = false
            }
        })

        // 비밀번호 조건 확인 코드
        et_pwd.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val pwd = et_pwd.text.toString()

                if(pwd.length in 8..15) {
                    if(isPasswordFormat(pwd)) {
                        tv_pwd_res.text = "비밀번호가 조건에 맞습니다!"
                        tv_pwd_res.setTextColor(Color.BLUE)
                    }
                    else {
                        tv_pwd_res.text = "비밀번호가 조건에 맞지 않습니다!"
                        tv_pwd_res.setTextColor(Color.RED)
                    }
                }
                else {
                    tv_pwd_res.text = "비밀번호가 조건에 맞지 않습니다!"
                    tv_pwd_res.setTextColor(Color.RED)
                }
            }
        })

        // 비밀번호 확인 코드
        et_pwd_check.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val pwd = et_pwd.text.toString()
                val check_pwd = et_pwd_check.text.toString()
                if(check_pwd.isNotEmpty()) {
                    if(pwd.length != check_pwd.length) {
                        tv_pwd_check_res.text = "비밀번호가 일치하지 않습니다!"
                        tv_pwd_check_res.setTextColor(Color.RED)
                    }
                    else {
                        if(pwd == check_pwd) {
                            tv_pwd_check_res.text = "비밀번호가 일치합니다!"
                            tv_pwd_check_res.setTextColor(Color.BLUE)
                        }
                    }
                }
                else {
                    tv_pwd_check_res.text = "비밀번호가 일치하지 않습니다!"
                    tv_pwd_check_res.setTextColor(Color.RED)
                }
            }
        })

        // 아이디 중복 확인 코드
        btn_check.setOnClickListener {
            val id = et_id.text.toString()
            registerService.checkRepitition(id).enqueue(object : Callback<Repetition> {
                override fun onResponse(call: Call<Repetition>, response: Response<Repetition>) {
                    repitition = response.body()
                    when (repitition?.result){
                        "exist" -> {
                            Toast.makeText(applicationContext,"해당 아이디는 이미 존재합니다!",Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            repetitionCheck = true
                            Toast.makeText(applicationContext,"해당 아이디는 사용할 수 있습니다!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<Repetition>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }

        // 회원 가입 코드
        btn_register.setOnClickListener {
            when(repetitionCheck) {
                true -> {
                    val id = et_id.text.toString()
                    val pwd = et_pwd.text.toString()
                    val name = et_name.text.toString()
                    registerService.requestRegister(id,pwd,name).enqueue(object : Callback<Register> {
                        override fun onResponse(call: Call<Register>, response: Response<Register>) {
                            register = response.body()
                            when (register?.code) {
                                "200" -> {
                                    et_pwd.setText("")
                                    et_pwd_check.setText("")
                                    et_id.setText("")
                                    et_name.setText("")

                                    Toast.makeText(applicationContext,"회원가입이 정상적으로 완료됐습니다!",Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(applicationContext,"회원가입 도중 오류가 발생했습니다!",Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(applicationContext,"아이디 중복 확인을 해주십시오!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 비밀번호 정규식
    fun isPasswordFormat(password: String): Boolean {
        return password.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$".toRegex())
    }
}


