package com.traceon.batur.ui.baseline.lahan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.App
import com.traceon.batur.R
import com.traceon.batur.data.db.delete
import com.traceon.batur.data.db.query
import com.traceon.batur.data.db.queryFirst
import com.traceon.batur.data.model.Lahan
import com.traceon.batur.data.model.LahanFields
import com.traceon.batur.data.model.Petani
import com.traceon.batur.data.model.PetaniFields
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.ActivityLahanBinding
import com.traceon.batur.ui.base.BaseActivity
import com.traceon.batur.ui.base.ScanQrCode
import com.traceon.batur.ui.baseline.komoditas.BaselineActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.traceon.batur.utils.SoftInputAssist
import kotlinx.android.synthetic.main.dialog_tambah_lahan.view.*
import kotlinx.android.synthetic.main.toolbar.*

class LahanActivity : BaseActivity<ActivityLahanBinding, ViewModel>(),
    LahaniAdapter.OnItemCheckListener {
    private var responseLogin: ResponseLogin? = null
    private lateinit var softInputAssist: SoftInputAssist
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var adapter: LahaniAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var etCodeQr: EditText
    private var listLahan = ArrayList<Lahan>()
    private var petani: Petani? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.activity_lahan

    override fun init() {
        setSupportActionBar(toolbar)
        Helper.setStatusBar(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        adapter = LahaniAdapter(listLahan, this) { lahan ->
            val i = Intent(this, BaselineActivity::class.java)
            i.putExtra(AppConstant.LAHAN, lahan)
            startActivity(i)
            overridePendingTransition(
                R.anim.enter, R.anim.exit
            )
        }

        skeletonScreen = Skeleton.bind(getDataBinding().rvLahan)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_lahan_skeleton)
            .show()

        responseLogin = Helper.getSesiLogin(this)
        softInputAssist = SoftInputAssist(this)

        petani = intent?.getParcelableExtra(AppConstant.PETANI)

        getDataBinding().tvNama.text = petani?.nama
        getDataBinding().tvAlamat.text = petani?.alamat

        Glide.with(this)
            .load(petani?.foto_petani)
            .apply(Helper.requestOperation)
            .into(getDataBinding().ivPetani)

        try {
            loadData(petani?.ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().slLahan.setOnRefreshListener {
            loadData(petani?.ID)
        }

        getDataBinding().fabPetani.setOnClickListener {
            onLahan()
        }
        getDataBinding().ivPetani.setOnClickListener {
            val i = Intent(this, ManagePetaniActivity::class.java)
            startActivityForResult(i, AppConstant.ACTION_ADD)
        }
        supportActionBar?.title = getString(R.string.daftar_lahan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun onLahan() {
        val dialog = AlertDialog.Builder(this)
        val sheet = layoutInflater.inflate(R.layout.dialog_tambah_lahan, null)
        val prefix = "L" + Helper.getString(AppConstant.KODE, this, null)

        etCodeQr = sheet.et_kode
        sheet.tv_prefix.text = prefix
        dialog.setTitle(getString(R.string.tambah_lahan))
        dialog.setView(sheet)
        alertDialog = dialog.show()
        sheet.bt_batal.setOnClickListener {
            alertDialog.dismiss()
        }
        sheet.bt_lanjut.setOnClickListener {
            if (etCodeQr.text.isNotBlank()) {
                val lahan = Lahan().queryFirst {
                    equalTo(
                        LahanFields.KODE,
                        prefix.plus(etCodeQr.text.toString())
                    )
                }
                if (lahan != null && (lahan.kode == prefix.plus(etCodeQr.text))) {
                    Helper.pesanBox(
                        this,
                        getString(R.string.error),
                        getString(R.string.kode_sudah_digunakan)
                    )
                } else {
                    alertDialog.dismiss()
                    val i = Intent(this, ManageLahanActivity::class.java)
                    i.putExtra(
                        AppConstant.ID,
                        prefix.plus(sheet.et_kode.text.toString())
                    )
                    startActivity(i)
                    overridePendingTransition(
                        R.anim.enter,
                        R.anim.exit
                    )
                }
            } else {
                Helper.pesanBox(
                    this,
                    getString(R.string.error),
                    getString(R.string.data_belum_lengkap)
                )
            }
        }
        sheet.iv_scan.setOnClickListener {
            val i = Intent(this, ScanQrCode::class.java)
            startActivityForResult(i, AppConstant.SCAN_QR)
        }
    }

    private fun loadData(id: Int?) {
        listLahan.clear()
        listLahan.addAll(Lahan().query { equalTo(LahanFields.PETANI_ID, id) })
        adapter.notifyDataSetChanged()
        if (listLahan.size > 0) {
            skeletonScreen.hide()
            getDataBinding().slLahan.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.SCAN_QR && resultCode == Activity.RESULT_OK) {
            val res = data?.getStringExtra(AppConstant.DATA).toString()
            etCodeQr.setText(res)
        }
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

    override fun onHapus(lahan: Lahan) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.konfirmasi))
            .setContentText(getString(R.string.yakin_mau_hapus))
            .setConfirmText(getString(R.string.ya))
            .setConfirmClickListener { sad ->
                Lahan().delete { equalTo(LahanFields.ID, lahan.ID) }
                val hasil = Petani().queryFirst { equalTo(PetaniFields.ID, petani?.ID) }
                hasil?.lahan_total = hasil?.lahan_total?.minus(1)
                sad.dismissWithAnimation()
            }
            .setCancelText(getString(R.string.tidak))
            .setOnCancelListener { sad ->
                sad.dismiss()
            }
    }

    override fun onEdit(lahan: Lahan) {

    }

    override fun onNomor(lahan: Lahan) {

    }
}