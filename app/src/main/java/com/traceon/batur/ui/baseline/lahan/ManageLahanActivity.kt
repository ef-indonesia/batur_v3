package com.traceon.batur.ui.baseline.lahan

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import com.traceon.batur.R
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityManageLahanBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.toolbar.*

class ManageLahanActivity : BaseActivity<ActivityManageLahanBinding, ViewModel>() {
    private lateinit var softInputAssist: SoftInputAssist
    private var responseLogin: ResponseLogin? = null
    private var kodeLahan: String? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_manage_lahan

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)
        kodeLahan = intent?.getStringExtra(AppConstant.ID)

        getDataBinding().tvManagementUnit.text = responseLogin?.manajemen_unit
        getDataBinding().tvArea.text = responseLogin?.area
        getDataBinding().tvDesa.text = responseLogin?.desa

        supportActionBar?.title =
            if (intent == null) getString(R.string.tambah_lahan) else getString(
                R.string.edit_lahan
            )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                overridePendingTransition(
                    R.anim.enter,
                    R.anim.exit
                )
                finish()
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