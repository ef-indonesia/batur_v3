package com.traceon.batur.ui.visit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bytcode.lib.spinner.multiselectspinner.data.KeyPairBoolData
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.FrequentAdapter
import com.traceon.batur.data.adapter.VisitAdapter
import com.traceon.batur.data.model.Frequent
import com.traceon.batur.data.model.TempFrequent
import com.traceon.batur.data.model.Visit
import com.traceon.batur.data.model.VisitDetail
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.*
import com.traceon.batur.databinding.ActivityVisitBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.ui.base.ScanQrCode
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class VisitActivity : BaseActivity<ActivityVisitBinding, VisitViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var adapter: VisitAdapter
    private lateinit var adapterFrequent: FrequentAdapter
    private var listVisit: ArrayList<Visit> = ArrayList()
    private var listFrequent: ArrayList<TempFrequent> = ArrayList()
    private val viewModel: VisitViewModel by viewModels()
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var skeletonFrequent: SkeletonScreen
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var dialog: SweetAlertDialog
    private lateinit var idDesa: String
    private lateinit var idKomoditas: String
    private lateinit var idFase: String
    private lateinit var idProgram: String
    private lateinit var program: String
    private lateinit var komoditas: String
    private var frequent: Int? = 0

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_visit

    @SuppressLint("CheckResult")
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
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = getString(R.string.loading)
        dialog.setCancelable(false)

        getDataBinding().spProgram.isEnabled = false
        getDataBinding().spKomoditas.isEnabled = false
        getDataBinding().spFase.isEnabled = false

        adapter = VisitAdapter(listVisit, viewModel) { vi ->
            getVisitDetal(vi.kode_lahan.toString())
        }

        adapterFrequent = FrequentAdapter(listFrequent) { fr ->
            loadData(idDesa, listFrequent.lastIndexOf(fr).toString())
        }

        skeletonScreen = Skeleton.bind(getDataBinding().rvVisit)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_visit_skeleton)
            .show()

        skeletonFrequent = Skeleton.bind(getDataBinding().rvFrekuensi)
            .adapter(adapterFrequent)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_frekuensi_skeleton)
            .show()

        idDesa = intent?.getStringExtra(AppConstant.ID_DESA).toString()
        val mu = intent?.getStringExtra(AppConstant.MU).toString()
        val area = intent?.getStringExtra(AppConstant.AREA).toString()
        val desa = intent?.getStringExtra(AppConstant.NAMA_DESA).toString()

        getDataBinding().etMu.setText(mu)
        getDataBinding().etArea.setText(area)
        getDataBinding().etDesa.setText(desa)

        try {
            Helper.hasInternetConnection().subscribe { hasInternet ->
                if (hasInternet) {
                    getProgram()
                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.cek_koneksi))
                        .setContentText(getString(R.string.coba_lagi))
                        .setConfirmClickListener {
                            it.dismiss()
                            dialog.dismiss()
                            getProgram()
                        }
                        .show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().btSearch.setOnClickListener {
            getVisitDetal(getDataBinding().etSearch.text?.trim().toString())
        }

        getDataBinding().rlQr.setOnClickListener {
            val i = Intent(this, ScanQrCode::class.java)
            startActivityForResult(i, AppConstant.SCAN_QR)
        }

        getDataBinding().slVisit.setOnRefreshListener {
            loadData(idDesa, frequent.toString())
        }

        getDataBinding().slFrekuensi.setOnRefreshListener {
            getFrequent(idDesa)
        }

        supportActionBar?.title =
            getString(R.string.visit).plus(" ").plus(getString(R.string.desa)).plus(" $desa")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("CheckResult")
    private fun getProgram() {
        dialog.show()
        repo.getProgramBaru(responseLogin?.database.toString(), null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setProgram(response)
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun setProgram(response: ResponseProgramBaru?) {
        val dataProgram: ArrayList<KeyPairBoolData> = ArrayList()
        response?.forEach { p ->
            val h = KeyPairBoolData()
            h.id = p.ID.toLong()
            h.name = p.nama
            h.isSelected = false
            dataProgram.add(h)
        }
        getDataBinding().tvProgram.visibility = View.GONE
        getDataBinding().spProgram.isEnabled = true
        getDataBinding().spProgram.setItems(dataProgram, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    getKomoditas(keyPairBoolData.id.toString())
                    program = keyPairBoolData.name
                    idProgram = keyPairBoolData.id.toString()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getKomoditas(idProgram: String) {
        dialog.show()
        repo.getKomoditasBaru(responseLogin?.database.toString(), idProgram)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setKomoditas(response)
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun setKomoditas(responseBaru: ResponseKomoditasBaru?) {
        val dataKomoditas: ArrayList<KeyPairBoolData> = ArrayList()
        responseBaru?.forEach { k ->
            val h = KeyPairBoolData()
            h.id = k.ID.toLong()
            h.name = k.nama
            h.isSelected = false
            dataKomoditas.add(h)
        }
        getDataBinding().tvKomoditas.visibility = View.GONE
        getDataBinding().spKomoditas.isEnabled = true
        getDataBinding().spKomoditas.setItems(dataKomoditas, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    getFase(keyPairBoolData.id.toString())
                    idKomoditas = keyPairBoolData.id.toString()
                    komoditas = keyPairBoolData.name
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getFase(idKomoditas: String) {
        dialog.show()
        repo.getFase(responseLogin?.database.toString(), idKomoditas)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    serFace(response)
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun serFace(response: ResponseFase?) {
        val dataFase: ArrayList<KeyPairBoolData> = ArrayList()
        response?.forEach { f ->
            val h = KeyPairBoolData()
            h.id = f.ID.toString().toLong()
            h.name = f.nama
            h.isSelected = false
            dataFase.add(h)
        }
        getDataBinding().tvFase.visibility = View.GONE
        getDataBinding().spFase.isEnabled = true
        getDataBinding().spFase.setItems(dataFase, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    idFase = keyPairBoolData.id.toString()
                    getFrequent(idDesa)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getFrequent(desaId: String) {
        dialog.show()
        skeletonFrequent.show()
        repo.getFrequentVisit(responseLogin?.database.toString(), desaId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response?.result == true) {
                        setFrequent(response.data)
                        getDataBinding().clProgram.visibility = View.VISIBLE
                        skeletonFrequent.hide()
                    } else {
                        getDataBinding().clProgram.visibility = View.GONE
                        Helper.pesanBox(this, getString(R.string.error), response.message)
                    }
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                    getDataBinding().slFrekuensi.isRefreshing = false
                }
            )
    }

    private fun setFrequent(data: Frequent) {
        getDataBinding().tvTglFrekuensi.text = "Data visit per tanggal : ${data.last_visit}"
        listFrequent.clear()
//        listFrequent.add(TempFrequent(getString(R.string.nol), data.total_frekuensi_0))
//        listFrequent.add(TempFrequent(getString(R.string.satu), data.total_frekuensi_1))
//        listFrequent.add(TempFrequent(getString(R.string.dua), data.total_frekuensi_2))
//        listFrequent.add(TempFrequent(getString(R.string.tiga), data.total_frekuensi_3))
//        listFrequent.add(TempFrequent(getString(R.string.empat), data.total_frekuensi_4))
//        listFrequent.add(TempFrequent(getString(R.string.lima), data.total_frekuensi_5))
        adapterFrequent.notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    private fun getVisitDetal(idLahan: String) {
        dialog.show()
        repo.getIncidentalVisit(responseLogin?.database.toString(), idLahan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response?.result == true) {
                        setVisitDetail(response.data)
                    } else {
                        SweetAlertDialog(this@VisitActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.error))
                            .setContentText(response.message)
                            .show()
                    }
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun setVisitDetail(data: VisitDetail) {
        val i = Intent(this@VisitActivity, InputVisitActivity::class.java)
        i.putExtra(AppConstant.ID_PROGRAM, idProgram)
        i.putExtra(AppConstant.ID_KOMODITAS, idKomoditas)
        i.putExtra(AppConstant.PROGRAM, program)
        i.putExtra(AppConstant.KOMODITAS, komoditas)
        i.putExtra(AppConstant.DATA, data)
        startActivity(i)
        overridePendingTransition(
            R.anim.enter,
            R.anim.exit
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
    private fun loadData(desa_id: String, frequent: String) {
        skeletonScreen.show()
        dialog.show()

        repo.getPrioritasVisitBaru(responseLogin?.database.toString(), desa_id, frequent)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.result == true) {
                        setData(response.data)
                        getDataBinding().clVisit.visibility = View.VISIBLE
                        skeletonScreen.hide()
                    } else {
                        getDataBinding().clVisit.visibility = View.GONE
                        Helper.pesanBox(
                            this,
                            getString(R.string.error),
                            response.message.toString()
                        )
                    }
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismissWithAnimation()
                    getDataBinding().svVisit.fullScroll(View.FOCUS_DOWN)
                }
            )
    }

    private fun setData(data: List<Visit>?) {
        val warna = resources.getStringArray(R.array.warna)
        var no = 0
        listVisit.clear()
        data?.forEach { vist ->
            no++
            vist.no_urut = no
            vist.warna = warna[no.minus(1)]
            listVisit.add(vist)
        }

        adapter.notifyDataSetChanged()
        skeletonScreen.hide()
        getDataBinding().slVisit.isRefreshing = false
        if (data?.size == 0) getDataBinding().empty.visibility =
            View.VISIBLE else getDataBinding().empty.visibility = View.GONE
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