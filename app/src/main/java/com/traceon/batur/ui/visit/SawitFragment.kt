package com.traceon.batur.ui.visit

import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentSawitBinding
import com.traceon.batur.ui.base.BaseFragment

class SawitFragment: BaseFragment<FragmentSawitBinding, VisitViewModel>() {
    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_sawit

    override fun init() {

    }
}