package com.traceon.batur.ui.baseline

import android.annotation.SuppressLint
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.adapter.BaselineAdapter
import com.traceon.batur.data.model.*
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponsePetani
import com.traceon.batur.databinding.ActivityPilihBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.saveAll
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*

class PilihActivity : BaseActivity<ActivityPilihBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var dialog: SweetAlertDialog
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var disposable: Disposable
    private lateinit var adapter: BaselineAdapter
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private var listFarmer: ArrayList<Farmer> = ArrayList()
    private var listPetani: ArrayList<Petani> = ArrayList()
    private var listReferral: ArrayList<Referral> = ArrayList()
    private var listKomoditas: ArrayList<TempKomoditas> = ArrayList()
    private var listLahan: ArrayList<Lahan> = ArrayList()
    private var listBaseline: ArrayList<Baseline> = ArrayList()
    private var listPilihan: ArrayList<Petani> = ArrayList()

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_pilih

    @SuppressLint("CheckResult")
    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        idDesa = intent?.getStringExtra(AppConstant.ID_DESA).toString()
        mDesa = intent?.getStringExtra(AppConstant.NAMA_DESA).toString()

        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = getString(R.string.loading)
        dialog.setCancelable(false)

        adapter = BaselineAdapter(listFarmer, object : BaselineAdapter.OnItemCheckListener {
            override fun onItemCheck(item: Farmer) {
                hitungTotal()
            }

            override fun onItemUnCheck(item: Farmer) {
                hitungTotal()
            }
        })

        skeletonScreen = Skeleton.bind(getDataBinding().rvBaseline)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_farmer_skeleton)
            .show()

        try {
            Helper.hasInternetConnection().subscribe { hasInternet ->
                if (hasInternet) {
                    loadPetani(idDesa)
                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.cek_koneksi))
                        .setContentText(getString(R.string.coba_lagi))
                        .setConfirmClickListener {
                            it.dismiss()
                            loadPetani(idDesa)
                        }
                        .show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().slBaseline.setOnRefreshListener {
            loadPetani(idDesa)
        }

        getDataBinding().cbAll.setOnCheckedChangeListener { _, isChecked ->
            listFarmer.forEach { f ->
                if (!isChecked) {
                    f.foto = isChecked
                    getDataBinding().swImage.isChecked = isChecked
                }
                f.dipilih = isChecked
            }
            hitungTotal()
            adapter.notifyDataSetChanged()
        }

        getDataBinding().btSynchronize.setOnClickListener {
            onSinkronisai()
        }

        getDataBinding().tvJudul.text = getString(R.string.desa).plus(" $mDesa")
        getDataBinding().tvDataDesa.text = getString(R.string.total_semua, mDesa)

        supportActionBar?.title = getString(R.string.pilih_petani)
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

    private fun onSinkronisai() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val idPilihan = ArrayList<String>()

        listFarmer.forEach { f ->
            val petani = listPetani[listFarmer.indexOf(f)]
            if (f.dipilih) listPilihan.add(petani)
        }

        listPilihan.forEach { p ->
            idPilihan.add(p.ID.toString())
        }

        Referral().deleteAll()
        Lahan().deleteAll()
        Baseline().deleteAll()
        Komoditas().deleteAll()

        getTerpilih(idPilihan.joinToString())
    }

    private fun getTerpilih(idPilihan: String) {
        dialog.show()
        disposable = Observable.zip(
            repo.getReferral(responseLogin?.database.toString(), responseLogin?.user_ID.toString()),
            repo.getKomoditas(responseLogin?.database.toString(), null),
            repo.getLahan(responseLogin?.database.toString(), idPilihan),
            repo.getBaseline(responseLogin?.database, idPilihan),
            { t1, t2, t3, t4 ->
                listReferral.addAll(t1)
                listKomoditas.addAll(t2)
                listLahan.addAll(t3)
                listBaseline.addAll(t4)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe(
                { response ->
                    if (response) {
                        setReferral(listReferral)
                        setKomoditas(listKomoditas)
                        setLahan(listLahan)
                        setBaseline(listBaseline)
                    }
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    onFinish()
                }
            )
    }

    private fun setBaseline(data: ArrayList<Baseline>) {
        data.forEach { baseline ->
            listOf(baseline).saveAll()
        }
    }

    private fun setLahan(data: ArrayList<Lahan>) {
        data.forEach { lahan ->
            listOf(lahan).saveAll()
        }
    }

    private fun setKomoditas(data: ArrayList<TempKomoditas>) {
        data.forEach { komo ->
            val komoditas = Komoditas()
            komoditas.ID = komo.ID
            komoditas.nama = komo.nama
            komoditas.kode_satuan = komo.kode_satuan
            komoditas.satuan = Gson().toJson(komo.satuan)
            listOf(komoditas).saveAll()
        }
    }

    private fun setReferral(data: ArrayList<Referral>) {
        data.forEach { referral ->
            listOf(referral).saveAll()
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPetani(desaId: String) {
        skeletonScreen.show()
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        repo.getPetani(responseLogin?.database.toString(), desaId, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setPetani(response)
                    skeletonScreen.hide()
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    getDataBinding().slBaseline.isRefreshing = false
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            )
    }

    private fun setPetani(response: ResponsePetani?) {
        var sizeAll = 0f
        response?.forEach { p ->
            listFarmer.add(
                Farmer(
                    p.ID.toString(),
                    p.nama.toString(),
                    p.alamat.toString(),
                    p.ukuran_text,
                    p.ukuran_total,
                    dipilih = false,
                    foto = false,
                    lahanTotal = response.indexOf(p).plus(1).toString(),
                    lahanKunci = response.indexOf(p).toString()
                )
            )
            listPetani.add(p)
            sizeAll += p.ukuran_total?.toFloat() ?: 0f
        }
        adapter.notifyDataSetChanged()
        getDataBinding().tvTotal.text = Helper.formatCapacity(sizeAll.toDouble(), 2)
        getDataBinding().tvMemory.text = Helper.spaceDisk(this)
    }

    private fun hitungTotal() {
        var total = 0.0
        listFarmer.forEach { f ->
            if (f.dipilih) {
                total += f.ukuranTotal.toString().toDouble()
            }
        }
        getDataBinding().btSynchronize.isEnabled = total > 0
        getDataBinding().tvTerpilih.text = Helper.formatCapacity(total, 2)
    }

    private fun onFinish() {
        disposable.dispose()
        dialog.dismiss()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Helper.saveBool(AppConstant.BASELINE, true, this)
        Toast.makeText(this, getString(R.string.synchronize), Toast.LENGTH_SHORT).show()
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