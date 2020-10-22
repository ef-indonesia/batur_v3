package com.traceon.batur.ui.visit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.VisitAdapter
import com.traceon.batur.data.model.Visit
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityVisitBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.ui.base.ScanQrCode
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class VisitActivity : BaseActivity<ActivityVisitBinding, VisitViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var adapter: VisitAdapter
    private var listVisit: ArrayList<Visit> = ArrayList()
    private val viewModel: VisitViewModel by viewModels()
    private lateinit var skeletonScreen: SkeletonScreen

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_visit

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(this)

        adapter = VisitAdapter(listVisit, viewModel) { vi ->
            toVisitDetal(vi.kode_lahan.toString())
        }

        skeletonScreen = Skeleton.bind(getDataBinding().rvVisit)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_visit_skeleton)
            .show()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        val idDesa = intent?.getStringExtra(AppConstant.ID_DESA).toString()
        val mu = intent?.getStringExtra(AppConstant.MU).toString()
        val area = intent?.getStringExtra(AppConstant.AREA).toString()
        val desa = intent?.getStringExtra(AppConstant.NAMA_DESA).toString()

        getDataBinding().tvMu.text = mu
        getDataBinding().tvArea.text = area
        getDataBinding().tvDesa.text = desa

        try {
            loadData(idDesa)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().btSearch.setOnClickListener {
            toVisitDetal(getDataBinding().etSearch.text?.trim().toString())
        }

        getDataBinding().rlQr.setOnClickListener {
            val i = Intent(this, ScanQrCode::class.java)
            startActivityForResult(i, AppConstant.SCAN_QR)
        }

        getDataBinding().slVisit.setOnRefreshListener {
            loadData(idDesa)
        }

        supportActionBar?.title = getString(R.string.visit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("CheckResult")
    private fun toVisitDetal(idLahan: String) {
        repo.getIncidentalVisit(responseLogin?.database.toString(), idLahan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response?.result == true) {
                        Log.d("VISIT DETAIL", "INCIDENTAL BRO")
                        val i = Intent(this@VisitActivity, InputVisitActivity::class.java)
                        i.putExtra(AppConstant.ID, idLahan)
                        startActivity(i)
                        overridePendingTransition(
                            R.anim.enter,
                            R.anim.exit
                        )
                    } else {
                        SweetAlertDialog(this@VisitActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.error))
                            .setContentText(response.message)
                            .show()
                    }
                },
                { t ->
                    t.printStackTrace()
                }
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.SCAN_QR && resultCode == Activity.RESULT_OK) {
            val res = data?.getStringExtra(AppConstant.DATA).toString()
            getDataBinding().etSearch.setText(res)
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData(desa_id: String) {
        val warna = resources.getStringArray(R.array.warna)
        repo.getPrioritasVisit(responseLogin?.database ?: return, desa_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.result == true) {
                        var no = 0
                        listVisit.clear()
                        response.data?.forEach { vist ->
                            no++
                            vist.no_urut = no
                            vist.warna = warna[no.minus(1)]
                            listVisit.add(vist)
                        }

                        adapter.notifyDataSetChanged()
                        skeletonScreen.hide()
                        getDataBinding().slVisit.isRefreshing = false
                        if (response.data?.size == 0) getDataBinding().empty.visibility =
                            View.VISIBLE else getDataBinding().empty.visibility = View.GONE
                    } else {
                        SweetAlertDialog(this@VisitActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.error))
                            .setContentText(response.message)
                            .show()
                    }
                },
                { t ->
                    t.printStackTrace()
                }
            )
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
}