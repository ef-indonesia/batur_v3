package com.traceon.batur.ui.visit

import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.PhotoAdapter
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseJenisKomoditas
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityInputVisitBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
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

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_input_visit

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

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
        loadFragment(SawitFragment())
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
    }

    private fun setKomoditas(listKomoditas: List<ResponseJenisKomoditas>?) {
        val komoditas: ArrayList<String> = ArrayList()
        listKomoditas?.forEach { kom ->
            komoditas.add(kom.nama.toString())
        }
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }
}