package com.traceon.batur.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.ui.login.LoginActivity
import com.traceon.batur.utils.Helper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var skeletonScreen: SkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Helper.setStatusBar(this)

        Helper.setLightStatusBar(window.decorView, this, true)

        skeletonScreen = Skeleton.bind(iv_logo)
            .load(R.layout.activity_splash)
            .duration(1000)
            .color(R.color.skeleton_shimer)
            .show()

        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                cekLogin()
            }

        }.start()
    }

    private fun cekLogin() {
        val i = if (Helper.getSesiLogin(this).result) Intent(
            applicationContext,
            MainActivity::class.java
        ) else Intent(applicationContext, LoginActivity::class.java)

        startActivity(i)
        overridePendingTransition(
            R.anim.enter,
            R.anim.exit
        )
        finish()
    }

}