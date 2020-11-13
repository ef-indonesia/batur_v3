package com.traceon.batur.ui.baseline.lahan

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.traceon.batur.R
import com.traceon.batur.databinding.ActivityLahanBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.toolbar.*

class LahanActivity : BaseActivity<ActivityLahanBinding, LahanViewModel>() {
    private lateinit var softInputAssist: SoftInputAssist

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_lahan

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        softInputAssist = SoftInputAssist(this)

        supportActionBar?.setHomeAsUpIndicator(
            Helper.setToolbarIconColor(
                this,
                R.drawable.ic_arrow_back
            )
        )

        supportActionBar?.title = getString(R.string.lahan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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