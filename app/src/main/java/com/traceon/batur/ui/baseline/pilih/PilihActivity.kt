package com.traceon.batur.ui.baseline.pilih

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.db.deleteAll
import com.traceon.batur.data.db.saveAll
import com.traceon.batur.data.model.*
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponsePetani
import com.traceon.batur.databinding.ActivityPilihBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*

class PilihActivity : BaseActivity<ActivityPilihBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var disposable: Disposable
    private lateinit var adapter: PilihAdapter
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private lateinit var mKode: String
    private lateinit var progressDialog: ProgressDialog
    private var listPilih: ArrayList<Pilih> = ArrayList()
    private var listPetani: ArrayList<Petani> = ArrayList()
    private var listReferral: ArrayList<Referral> = ArrayList()
    private var listKomoditas: ArrayList<TempKomoditas> = ArrayList()
    private var listLahan: ArrayList<Lahan> = ArrayList()
    private var listBaseline: ArrayList<Baseline> = ArrayList()
    private var listBank: ArrayList<Bank> = ArrayList()
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
        mKode = intent?.getStringExtra(AppConstant.KODE).toString()

        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)
        progressDialog = ProgressDialog(this)

        adapter = PilihAdapter(listPilih, object : PilihAdapter.OnItemCheckListener {
            override fun onItemCheck(item: Pilih) {
                hitungTotal()
            }

            override fun onItemUnCheck(item: Pilih) {
                hitungTotal()
            }
        })

        skeletonScreen = Skeleton.bind(getDataBinding().rvBaseline)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_pilih_skeleton)
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
            listPilih.forEach { f ->
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
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSinkronisai() {
        Helper.showProgressDialogWithTitle(
            progressDialog, getString(R.string.please_wait), getString(
                R.string.connecting_to_server
            )
        )

        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val idPilihan = ArrayList<String>()

        Petani().deleteAll()
        Referral().deleteAll()
        Lahan().deleteAll()
        Baseline().deleteAll()
        Komoditas().deleteAll()
        Bank().deleteAll()

        listPilih.forEach { f ->
            val petani = listPetani[listPilih.indexOf(f)]
            if (f.dipilih) listPilihan.add(petani)
        }

        listPilihan.forEach { p ->
            idPilihan.add(p.ID.toString())
            listOf(p).saveAll()
        }

        getTerpilih(idPilihan.joinToString())
    }

    private fun getTerpilih(idPilihan: String) {
        disposable = Observable.zip(
            repo.getReferral(responseLogin?.database.toString(), responseLogin?.user_ID.toString()),
            repo.getKomoditas(responseLogin?.database.toString(), null),
            repo.getLahan(responseLogin?.database.toString(), idPilihan),
            repo.getBaseline(responseLogin?.database, idPilihan),
            repo.getBank(responseLogin?.database),
            { t1, t2, t3, t4, t5 ->
                listReferral.addAll(t1)
                listKomoditas.addAll(t2)
                listLahan.addAll(t3)
                listBaseline.addAll(t4)
                listBank.addAll(t5)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Helper.showProgressDialogWithTitle(
                    progressDialog,
                    getString(R.string.please_wait), "Terhubung ke server..."
                )
            }
            .doOnTerminate {}
            .subscribe(
                { response ->
                    if (response) {
                        setReferral(listReferral)
                        setKomoditas(listKomoditas)
                        setLahan(listLahan)
                        setBaseline(listBaseline)
                        setBank(listBank)
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

    private fun setBank(data: ArrayList<Bank>) {
        Helper.showProgressDialogWithTitle(
            progressDialog,
            getString(R.string.please_wait),
            "Memproses data ".plus("bank...")
        )
        data.forEach { bank ->
            listOf(bank).saveAll()
        }
    }

    private fun setBaseline(data: ArrayList<Baseline>) {
        Helper.showProgressDialogWithTitle(
            progressDialog,
            getString(R.string.please_wait),
            "Memproses data ".plus("baseline...")
        )
        data.forEach { baseline ->
            listOf(baseline).saveAll()
        }
    }

    private fun setLahan(data: ArrayList<Lahan>) {
        Helper.showProgressDialogWithTitle(
            progressDialog,
            getString(R.string.please_wait),
            "Memproses data ".plus("lahan...")
        )
        data.forEach { lahan ->
            listOf(lahan).saveAll()
        }
    }

    private fun setKomoditas(data: ArrayList<TempKomoditas>) {
        Helper.showProgressDialogWithTitle(
            progressDialog,
            getString(R.string.please_wait),
            "Memproses data ".plus("komoditas...")
        )
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
        Helper.showProgressDialogWithTitle(
            progressDialog,
            getString(R.string.please_wait),
            "Memproses data ".plus("referral...")
        )
        data.forEach { ref ->
            listOf(ref).saveAll()
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
            listPilih.add(
                Pilih(
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
        listPilih.forEach { f ->
            if (f.dipilih) {
                total += f.ukuranTotal.toString().toDouble()
            }
        }
        getDataBinding().btSynchronize.isEnabled = total > 0
        getDataBinding().tvTerpilih.text = Helper.formatCapacity(total, 2)
    }

    private fun onFinish() {
        disposable.dispose()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Helper.saveBool(AppConstant.BASELINE, true, this)
        Helper.saveString(AppConstant.KODE, mKode, this)
        Helper.hideProgressDialogWithTitle(progressDialog)
        val i = Intent()
        setResult(1, i)
        overridePendingTransition(
            R.anim.enter,
            R.anim.exit
        )
        finish()
    }

    private fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }

    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.konfirmasi))
            .setContentText(getString(R.string.menu_sebelumnya))
            .setConfirmText(getString(R.string.ya))
            .setConfirmClickListener { sad ->
                super.onBackPressed()
                sad.dismissWithAnimation()
                overridePendingTransition(
                    R.anim.enter,
                    R.anim.exit
                )
                finish()
            }
            .setCancelText(getString(R.string.tidak))
            .setOnCancelListener { sad ->
                sad.dismiss()
            }
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