package com.traceon.batur.ui.baseline.lahan

import android.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import com.traceon.batur.R
import com.traceon.batur.data.model.Petani
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityManagePetaniBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.toolbar.*

class ManagePetaniActivity : BaseActivity<ActivityManagePetaniBinding, ViewModel>() {
    private var responseLogin: ResponseLogin? = null
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var alertDialog: AlertDialog
    private var petani: Petani? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_manage_petani

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)

        supportActionBar?.title = getString(R.string.edit_petani)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.enter,
            R.anim.exit
        )
        finish()
    }

    override fun onResume() {
        super.onResume()
        softInputAssist.onResume()
    }

    override fun onPause() {
        super.onPause()
        softInputAssist.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        softInputAssist.onDestroy()
    }
}