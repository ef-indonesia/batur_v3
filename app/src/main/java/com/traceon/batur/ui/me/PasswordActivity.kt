package com.traceon.batur.ui.me

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponseUpdate
import com.traceon.batur.databinding.ActivityChangePasswordBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.ui.me.ProfileViewModel
import com.traceon.batur.utils.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : BaseActivity<ActivityChangePasswordBinding, ProfileViewModel>() {

    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_change_password

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(this)

        getDataBinding().etRePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotBlank()) {
                    if (p0.toString() == getDataBinding().etNewPassword.text.toString()) {
                        getDataBinding().etRePassword.error = null
                        getDataBinding().btPasswordSimpan.isEnabled = true
                    } else {
                        getDataBinding().etRePassword.error = "Password tidak sama"
                        getDataBinding().btPasswordSimpan.isEnabled = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        getDataBinding().btPasswordSimpan.setOnClickListener {
            onChangePassword()
        }

        supportActionBar?.title = "Ganti Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onChangePassword() {
        val oldPassword = getDataBinding().etOldPassword.text.toString()
        val newPassword = getDataBinding().etNewPassword.text.toString()
        val rePassword = getDataBinding().etRePassword.text.toString()
        val dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)

        if (oldPassword.isNotBlank() && newPassword.isNotBlank() && rePassword.isNotBlank()) {
            dialog.show()
            try {
                repo.ubahPassword(
                    responseLogin?.database ?: return,
                    responseLogin?.user_ID.toString(),
                    oldPassword,
                    newPassword
                ).enqueue(object : Callback<ResponseUpdate> {
                    override fun onResponse(
                        call: Call<ResponseUpdate>,
                        response: Response<ResponseUpdate>
                    ) {
                        response.let { res ->
                            if (res.isSuccessful) {
                                if (res.body()?.status == true) {
                                    SweetAlertDialog(
                                        this@PasswordActivity,
                                        SweetAlertDialog.SUCCESS_TYPE
                                    )
                                        .setTitleText(res.body()?.response)
                                        .setContentText(res.body()?.message)
                                        .setConfirmClickListener {
                                            it.dismiss()
                                            finish()
                                            overridePendingTransition(
                                                R.anim.enter,
                                                R.anim.exit
                                            )
                                        }
                                        .show()
                                } else {
                                    SweetAlertDialog(
                                        this@PasswordActivity,
                                        SweetAlertDialog.ERROR_TYPE
                                    )
                                        .setTitleText(res.body()?.response)
                                        .setContentText(res.body()?.message)
                                        .show()
                                }
                                dialog.dismiss()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseUpdate>, t: Throwable) {
                        dialog.dismiss()
                        t.printStackTrace()
                        SweetAlertDialog(
                            this@PasswordActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Error")
                            .setContentText(t.message)
                            .show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Error")
                .setContentText("Field tidak lengkap")
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(
                    R.anim.enter,
                    R.anim.exit
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }
}