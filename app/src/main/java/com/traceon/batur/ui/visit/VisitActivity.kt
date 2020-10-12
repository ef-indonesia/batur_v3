package com.traceon.batur.ui.visit

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.VisitAdapter
import com.traceon.batur.data.model.Visit
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponsePrioritasVisit
import com.traceon.batur.databinding.ActivityVisitBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        adapter = VisitAdapter(listVisit, viewModel)

        skeletonScreen = Skeleton.bind(getDataBinding().rvVisit)
            .adapter(adapter)
            .count(R.color.skeleton_shimer)
            .load(R.layout.item_visit_skeleton)
            .show()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        try {
            loadData("48")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().btIncidental.setOnClickListener {
            val i = Intent(this, InputVisitActivity::class.java)
            startActivity(i)
            overridePendingTransition(
                R.anim.enter,
                R.anim.exit
            )
        }

        getDataBinding().slVisit.setOnRefreshListener {
            loadData("48")
        }
        supportActionBar?.title = getString(R.string.visit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadData(desa_id: String) {
        val warna = resources.getIntArray(R.array.warna)
        repo.getPrioritasVisit(responseLogin?.database ?: return, desa_id)
            .enqueue(object : Callback<ResponsePrioritasVisit> {
                override fun onResponse(
                    call: Call<ResponsePrioritasVisit>,
                    response: Response<ResponsePrioritasVisit>
                ) {
                    response.let { res ->
                        if (res.isSuccessful) {
                            var no = 0
                            res.body()?.data?.forEach { vist ->
                                no++
                                vist.no_urut = no
                                vist.warna = warna[no]
                                listVisit.add(vist)
                            }

                            adapter.notifyDataSetChanged()
                            skeletonScreen.hide()
                            getDataBinding().slVisit.isRefreshing = false
                        }
                    }
                }

                override fun onFailure(call: Call<ResponsePrioritasVisit>, t: Throwable) {
                    t.printStackTrace()
                }

            })
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