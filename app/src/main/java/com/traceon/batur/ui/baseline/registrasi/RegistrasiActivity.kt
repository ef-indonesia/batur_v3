package com.traceon.batur.ui.baseline.registrasi

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import com.traceon.batur.R
import com.traceon.batur.databinding.ActivityRegisterBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*

class RegistrasiActivity : BaseActivity<ActivityRegisterBinding, ViewModel>() {
    private lateinit var softInputAssist: SoftInputAssist

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)
        Helper.setLightStatusBar(toolbar, this, true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_register)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        softInputAssist = SoftInputAssist(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean =
        NavHostFragment.findNavController(fm_register).navigateUp()

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

    fun setTitle(title: String) {
        supportActionBar?.title = title
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