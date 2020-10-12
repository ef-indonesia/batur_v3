package com.traceon.batur.ui.baseline

import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentBaselineBinding
import com.traceon.batur.ui.base.BaseFragment

class BaselineFragment : BaseFragment<FragmentBaselineBinding, BaselineViewModel>() {
    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_baseline

    override fun init() {

    }

}