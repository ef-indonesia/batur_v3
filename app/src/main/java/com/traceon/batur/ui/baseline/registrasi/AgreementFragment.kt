package com.traceon.batur.ui.baseline.registrasi

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentRegister1Binding
import com.traceon.batur.ui.base.BaseFragment

class AgreementFragment : BaseFragment<FragmentRegister1Binding, ViewModel>() {

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_register_1

    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        setHasOptionsMenu(true)
        (activity as RegisterActivity).setTitle("Agreement")

        val webSettings = getDataBinding().wvAgreement.settings
        webSettings.javaScriptEnabled = true
        getDataBinding().wvAgreement.loadUrl("file:///android_asset/agreement.html")
    }
}