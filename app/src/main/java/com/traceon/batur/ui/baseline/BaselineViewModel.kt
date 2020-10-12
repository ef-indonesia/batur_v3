package com.traceon.batur.ui.baseline

import androidx.hilt.lifecycle.ViewModelInject
import com.traceon.batur.data.repo.LocalRepository
import com.traceon.batur.ui.base.BaseViewModel

class BaselineViewModel @ViewModelInject constructor(
    private val local: LocalRepository
) : BaseViewModel() {
}