package com.traceon.batur.ui.visit

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.traceon.batur.data.model.Lahan
import com.traceon.batur.data.model.Visit
import com.traceon.batur.data.repo.LocalRepository
import com.traceon.batur.ui.base.BaseViewModel

class VisitViewModel @ViewModelInject constructor(
    private val localRepo: LocalRepository
) : BaseViewModel() {
    private val localData = MutableLiveData<Boolean>()
    private val lahan: LiveData<List<Lahan>> = Transformations.switchMap(localData) {
        localRepo.getAllLahan()
    }
    private val visit: LiveData<List<Visit>> = Transformations.switchMap(localData) {
        localRepo.getAllVisit()
    }

    fun getVisit(): LiveData<List<Visit>> {
        return visit
    }

    fun getLahan(): LiveData<List<Lahan>> {
        return lahan
    }
}