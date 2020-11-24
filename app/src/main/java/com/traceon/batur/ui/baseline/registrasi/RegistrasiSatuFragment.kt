package com.traceon.batur.ui.baseline.registrasi

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.gcacace.signaturepad.views.SignaturePad
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentRegister1Binding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.AppConstant
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class RegistrasiSatuFragment : BaseFragment<FragmentRegister1Binding, ViewModel>() {
    private var uriSignature: String? = null
    private lateinit var progressDialog: ProgressDialog

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_register_1

    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        setHasOptionsMenu(true)
        (activity as RegistrasiActivity).setTitle(getString(R.string.agreement))

        progressDialog = ProgressDialog(context)

        val webSettings = getDataBinding().wvAgreement.settings
        webSettings.javaScriptEnabled = true
        getDataBinding().wvAgreement.loadUrl("file:///android_asset/agreement.html")

        getDataBinding().signature.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                getDataBinding().ivClear.visibility = View.VISIBLE
                getDataBinding().btAgreement.isEnabled = true
            }

            override fun onClear() {
                getDataBinding().ivClear.visibility = View.GONE
                getDataBinding().btAgreement.isEnabled = false
            }
        })

        PushDownAnim.setPushDownAnimTo(getDataBinding().btAgreement)
            .setScale(PushDownAnim.MODE_STATIC_DP, 5f)
            .setOnClickListener { setNextPage(it) }

        getDataBinding().ivClear.setOnClickListener { v ->
            getDataBinding().signature.clear()
            v.visibility = View.GONE
            getDataBinding().btAgreement.isEnabled = false
        }
    }

    private fun setNextPage(view: View?) {
        val bmpSignature: Bitmap = getDataBinding().signature.signatureBitmap

        if (addJpgSignatureToGallery(bmpSignature)) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.agreement))
                .setContentText(getString(R.string.agreement_desc))
                .setConfirmText(getString(R.string.ya))
                .setConfirmClickListener {
                    if (uriSignature != null) {
                        it.dismissWithAnimation()
                        val bundle = Bundle()
                        bundle.putString(AppConstant.ARG_SIGNATURE, uriSignature)
                        view?.findNavController()?.navigate(R.id.step_biodata, bundle)
                        activity?.findViewById<Stepper>(R.id.stepper)?.forward()
                    }
                }
                .setCancelText(getString(R.string.tidak))
                .setCancelClickListener {
                    it.dismiss()
                }
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.findViewById<Stepper>(R.id.stepper)?.back()
                activity?.onBackPressed()
                activity?.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                true
            }
            else -> false
        }
    }

    private fun addJpgSignatureToGallery(signature: Bitmap?): Boolean {
        var result = false
        try {
            val photo = File(
                getAlbumStorageDir(AppConstant.DIR_NAME),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature!!, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun getAlbumStorageDir(albumName: String?): File? {
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).absolutePath, albumName.toString()
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    @Throws(IOException::class)
    private fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
//        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val newBitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri: Uri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        activity?.sendBroadcast(mediaScanIntent)
        uriSignature = contentUri.toString()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}