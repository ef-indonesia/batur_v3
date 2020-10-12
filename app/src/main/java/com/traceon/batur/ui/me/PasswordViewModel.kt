package com.traceon.batur.ui.me

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.traceon.batur.data.model.Lahan
import com.traceon.batur.data.repo.LocalRepository
import com.traceon.batur.ui.base.BaseViewModel

class ProfileViewModel @ViewModelInject constructor(
    private val localRepo: LocalRepository
) : BaseViewModel() {
    private val localData = MutableLiveData<Boolean>()
    private val lahanAll: LiveData<List<Lahan>> = Transformations.switchMap(localData) {
        localRepo.getAllLahan()
    }
}