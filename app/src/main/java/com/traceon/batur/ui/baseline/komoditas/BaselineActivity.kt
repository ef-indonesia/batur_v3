package com.traceon.batur.ui.baseline.komoditas

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.db.query
import com.traceon.batur.data.model.Baseline
import com.traceon.batur.data.model.BaselineFields
import com.traceon.batur.data.model.Lahan
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityBaselineBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.toolbar.*

class BaselineActivity : BaseActivity<ActivityBaselineBinding, ViewModel>() {
    private var responseLogin: ResponseLogin? = null
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var adapter: BaselineAdapter
    private var listBaseline: ArrayList<Baseline> = ArrayList()
    private var lahan: Lahan? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_baseline

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)

        lahan = intent?.getParcelableExtra(AppConstant.LAHAN)

        adapter = BaselineAdapter(listBaseline) { baseline ->
            val i = Intent(this, ManageBaselineActivity::class.java)
            i.putExtra(AppConstant.BASELINE, baseline)
            startActivityForResult(i, AppConstant.ACTION_EDIT)
        }

        skeletonScreen = Skeleton.bind(getDataBinding().rvBaseline)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_baseline_skeleton)
            .show()

        try {
            loadData(lahan)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().slBaseline.setOnRefreshListener {
            loadData(lahan)
        }

        supportActionBar?.title = getString(R.string.daftar_komoditas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadData(lahan: Lahan?) {
        listBaseline.clear()
        listBaseline.addAll(Baseline().query { equalTo(BaselineFields.KODE_LAHAN, lahan?.kode) })
        adapter.notifyDataSetChanged()
        if (adapter.itemCount > 0) {
            skeletonScreen.hide()
            getDataBinding().slBaseline.isRefreshing = false
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