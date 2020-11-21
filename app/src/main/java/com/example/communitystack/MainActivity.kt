package com.example.communitystack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.communitystack.ui.WallActivity
import com.example.communitystack.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val vm by lazy {
        createViewModel<MainViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PreferenceManager.instance.isUserLoggedIn){
            startActivity(Intent(this, WallActivity::class.java))
            finish()
        }

        login.setOnClickListener {
            if (!email.text.isNullOrBlank() && !password.text.isNullOrBlank()){
                vm.login(
                    user = User(
                        id = randomId(),
                        name = name.text.toString(),
                        email = email.text.toString()
                    ),
                    pass = password.text.toString()
                )
            }else{
                Snackbar.make(password, "Enter proper details to enter", Snackbar.LENGTH_SHORT)
            }
        }

        vm.login.observe(this){
            when(it){
                is State.Loading ->{
                    loading.isVisible = true
                }
                is State.Success ->{
                    loading.isVisible = false
                    PreferenceManager.instance.isUserLoggedIn = true
                    startActivity(Intent(this, WallActivity::class.java))
                    finish()
                }
                is State.Failed ->{
                    loading.isVisible = false
                    PreferenceManager.instance.isUserLoggedIn = false
                    Snackbar.make(password, it.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}