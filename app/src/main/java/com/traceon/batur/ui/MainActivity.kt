package com.traceon.batur.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.traceon.batur.R
import com.traceon.batur.data.db.deleteAll
import com.traceon.batur.data.model.*
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.ui.baseline.pilih.AreaFragment
import com.traceon.batur.ui.baseline.petani.PetaniFragment
import com.traceon.batur.ui.home.HomeFragment
import com.traceon.batur.ui.login.LoginActivity
import com.traceon.batur.ui.me.PasswordActivity
import com.traceon.batur.ui.pending.PendingFragment
import com.traceon.batur.ui.visit.dashboard.VisitFragment
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var doubleBackToExit: Boolean = false
    private var responseLogin: ResponseLogin? = null
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Helper.setStatusBar(this)

        Helper.setLightStatusBar(window.decorView, this, true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WAKE_LOCK
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    val path =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/batur/")
                    Helper.makeMediaFolder(path)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                }

            }).check()

        responseLogin = Helper.getSesiLogin(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        bottomNav = findViewById(R.id.bottom_nav)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
        navView.setNavigationItemSelectedListener(this)
        bottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.nav_baseline -> {
                    if (Helper.getBool(AppConstant.BASELINE, this)) {
                        loadFragment(PetaniFragment())
                    } else {
                        loadFragment(AreaFragment())
                    }
                }
                R.id.nav_visit -> {
                    loadFragment(VisitFragment())
                }
            }
            true
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
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
                Petani().deleteAll()
                Referral().deleteAll()
                Lahan().deleteAll()
                Baseline().deleteAll()
                Komoditas().deleteAll()
                Bank().deleteAll()
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

    fun setMenu(menu: Int) {
        when (menu) {
            0 -> {
                bottomNav.selectedItemId = R.id.nav_home
            }
            1 -> {
                bottomNav.selectedItemId = R.id.nav_baseline
            }
            2 -> {
                bottomNav.selectedItemId = R.id.nav_visit
            }
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    fun setTitleActionBar(title: String) {
        supportActionBar?.title = title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            loadFragment(PetaniFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
                true
            }
            R.id.nav_sync -> {
                loadFragment(PendingFragment())
                true
            }
            else -> true
        }
    }
}