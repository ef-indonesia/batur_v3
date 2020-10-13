package com.traceon.batur.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var repo: RemoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Helper.setStatusBar(this)

        Helper.setLightStatusBar(window.decorView, this, true)

        repo = RemoteRepository()
        bt_submit.setOnClickListener {
            onLogin()
        }
    }

    @SuppressLint("CheckResult")
    private fun onLogin() {
        val username = et_username.text.toString()
        val password = et_password.text.toString()

        if (username.isNotBlank() && password.isNotBlank()) {
            pb_login.visibility = View.VISIBLE
            repo.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { res ->
                        if (res.result) {
                            Helper.saveString(AppConstant.LOGIN, Gson().toJson(res), this)
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                            overridePendingTransition(
                                R.anim.enter,
                                R.anim.exit
                            )
                            finish()
                        } else {
                            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(res.status)
                                .setContentText(res.message)
                                .show()
                        }
                    },
                    { err ->
                        pb_login.visibility = View.GONE
                        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(err.message)
                            .show()
                        err.printStackTrace()
                    },
                    {
                        pb_login.visibility = View.GONE
                        Log.d("TAG", "Complete")
                    }
                )
        } else {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("Data belum lengkap")
                .show()
        }
    }
}