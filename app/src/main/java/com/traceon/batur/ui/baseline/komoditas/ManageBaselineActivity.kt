package com.traceon.batur.ui.baseline.komoditas

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.model.Baseline
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityManageBaselineBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.toolbar.*

class ManageBaselineActivity : BaseActivity<ActivityManageBaselineBinding, ViewModel>() {
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var skeletonScreen: SkeletonScreen
    private var responseLogin: ResponseLogin? = null
    private var baseline: Baseline? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_manage_baseline

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)

        baseline = intent?.getParcelableExtra(AppConstant.BASELINE)

        skeletonScreen = Skeleton.bind(getDataBinding().llContainer)
            .color(R.color.skeleton_shimer)
            .load(R.layout.activity_manage_baseline_skeleton)
            .show()

        try {
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().btSimpan.setOnClickListener {
            onSimpan()
        }

        supportActionBar?.title = getString(R.string.komoditas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onSimpan() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        onBackPressed()
    }

    private fun loadData() {
        if (baseline != null) {
            getDataBinding().tvKomoditas.text = baseline?.komoditas
            getDataBinding().tvKodeLahan.text = baseline?.kode_lahan
            getDataBinding().etNilai.setText(baseline?.nilai)
            getDataBinding().etLokasi.setText(baseline?.gps)
            baseline?.produktifitas?.split("-")?.forEach { data ->
//                getDataBinding().etProduksi.setText(data[0].toString())
//                getDataBinding().etPeriode.setText(data[1].toString())
            }
            skeletonScreen.hide()
        }
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