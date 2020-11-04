package com.traceon.batur.ui.visit

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import awesome.vrund.vpawesomewidgets.VPAutoCompleteTextView
import awesome.vrund.vpawesomewidgets.VPBaseAdapter
import awesome.vrund.vpawesomewidgets.VPSpinner
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.PhotoAdapter
import com.traceon.batur.data.model.CheckPoint
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseJenisKomoditas
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityInputVisitBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class InputVisitActivity : BaseActivity<ActivityInputVisitBinding, VisitViewModel>() {

    private lateinit var adapter: PhotoAdapter

    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private var listPhoto: ArrayList<Uri> = ArrayList()
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var softInputAssist: SoftInputAssist

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_input_visit

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        softInputAssist = SoftInputAssist(this)

        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(this)
        adapter = PhotoAdapter(listPhoto)

        skeletonScreen = Skeleton.bind(getDataBinding().rvVisitPhoto)
            .adapter(adapter)
            .load(R.layout.item_visit_photo_skeleton)
            .color(R.color.shimmer_color)
            .duration(1000)
            .show()

        try {
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        getDataBinding().btVisitSimpan.setOnClickListener {
            finish()
        }

        supportActionBar?.title = getString(R.string.input_visit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadData() {
        repo.getJenisKomoditas(responseLogin?.database.toString(), null)
            .enqueue(object : Callback<List<ResponseJenisKomoditas>> {
                override fun onResponse(
                    call: Call<List<ResponseJenisKomoditas>>,
                    response: Response<List<ResponseJenisKomoditas>>
                ) {
                    response.let { res ->
                        if (res.isSuccessful) {
                            setKomoditas(res.body())
                        } else {
                            SweetAlertDialog(this@InputVisitActivity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getString(R.string.error))
                                .setContentText("Gagal ambil komofitas")
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<ResponseJenisKomoditas>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        getChecklist()
    }

    @SuppressLint("CheckResult")
    private fun getChecklist() {
        repo.getCheckPoint(responseLogin?.database.toString(), "2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.result) {
                        setCheckPoint(response.data)
                    }
                },
                { t ->
                    t.printStackTrace()
                }
            )
    }

    private fun setCheckPoint(checkPoint: List<CheckPoint>) {
        val container = getDataBinding().llCheckPoint
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        checkPoint.forEach { cp ->
            val question: ArrayList<String> = ArrayList()
            question.addAll(cp.check_list)
            val ll = LinearLayout(container.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = layoutParams
            ll.setPadding(5, 5, 5, 5)
            ll.weightSum = 1f
            ll.gravity = Gravity.CENTER

            val tvLabel = TextView(container.context)
            tvLabel.typeface = Typeface.DEFAULT_BOLD
            tvLabel.layoutParams = layoutParams
            tvLabel.text = cp.check_point

            val adapter = VPBaseAdapter(this, question)
            val vpQuetion = VPSpinner(ll.context)
            vpQuetion.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_spinner, resources.newTheme())
            vpQuetion.layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
            vpQuetion.setAdapter(adapter)
            vpQuetion.setIconTint(Color.BLACK)
            !vpQuetion.hasLabel()
            vpQuetion.showLabel(false)

            val indikator = View(ll.context)
            indikator.layoutParams = LinearLayout.LayoutParams(
                50, 50, 1f
            )
            indikator.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_round, resources.newTheme())
            indikator.setBackgroundColor(Color.BLUE)
            indikator.setPadding(5, 5, 5, 5)

            ll.addView(vpQuetion)
            ll.addView(indikator)
            container.addView(tvLabel)
            container.addView(ll)
        }


    }

    private fun setKomoditas(listKomoditas: List<ResponseJenisKomoditas>?) {
        val komoditas: ArrayList<String> = ArrayList()
        val adapterKomoditas = VPBaseAdapter(this, komoditas)
        listKomoditas?.forEach { kom ->
            komoditas.add(kom.nama.toString())
        }
        adapterKomoditas.notifyDataSetChanged()
        getDataBinding().spPilihKomoditas.setAdapter(adapterKomoditas)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
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