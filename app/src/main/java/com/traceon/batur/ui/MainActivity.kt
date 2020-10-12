package com.traceon.batur.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.traceon.batur.R
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.ui.login.LoginActivity
import com.traceon.batur.ui.me.PasswordActivity
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var doubleBackToExit: Boolean = false
    private var responseLogin: ResponseLogin? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Helper.setStatusBar(this)

        Helper.setLightStatusBar(window.decorView, this, true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
        responseLogin = Helper.getSesiLogin(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_baseline,
                R.id.nav_visit
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNav.setupWithNavController(navController)
        navView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            responseLogin?.nama_lengkap
        navView.getHeaderView(0).findViewById<LinearLayout>(R.id.ll_sign_out).setOnClickListener {
            onSignout()
        }
        navView.getHeaderView(0).findViewById<LinearLayout>(R.id.ll_password).setOnClickListener {
            val i = Intent(this, PasswordActivity::class.java)
            startActivity(i)
            overridePendingTransition(
                R.anim.enter,
                R.anim.exit
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            finish()
        }
        this.doubleBackToExit = true
        Toast.makeText(
            applicationContext,
            getString(R.string.onbackpressed),
            Toast.LENGTH_SHORT
        ).show()
        Handler(Looper.myLooper()!!).postDelayed(Runnable { doubleBackToExit = false }, 2000)
    }

    private fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }

    private fun onSignout() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.konfirmasi))
            .setContentText(getString(R.string.yakin_keluar))
            .setConfirmText(getString(R.string.ya))
            .setConfirmClickListener {
                Helper.clearPreferences(this)
                val i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                overridePendingTransition(
                    R.anim.enter,
                    R.anim.exit
                )
                finish()
            }
            .setCancelButton(getString(R.string.tidak), null)
            .show()

    }
}