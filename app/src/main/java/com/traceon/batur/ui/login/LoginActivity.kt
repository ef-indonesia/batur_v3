package com.traceon.batur.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.databinding.ActivityLoginBinding
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginActivity : BaseActivity<ActivityLoginBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun init() {
        Helper.setStatusBar(this)

        Helper.setLightStatusBar(window.decorView, this, true)

        repo = RemoteRepository()
        getDataBinding().btSubmit.setOnClickListener {
            onLogin()
        }
    }

    @SuppressLint("CheckResult")
    private fun onLogin() {
        val username = getDataBinding().etUsername.text.toString()
        val password = getDataBinding().etPassword.text.toString()

        if (username.isNotBlank() && password.isNotBlank()) {
            getDataBinding().pbLogin.visibility = View.VISIBLE
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
                        getDataBinding().pbLogin.visibility = View.GONE
                        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.error))
                            .setContentText(err.message)
                            .show()
                        err.printStackTrace()
                    },
                    {
                        getDataBinding().pbLogin.visibility = View.GONE
                    }
                )
        } else {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.error))
                .setContentText(getString(R.string.data_belum_lengkap))
                .show()
        }
    }
}