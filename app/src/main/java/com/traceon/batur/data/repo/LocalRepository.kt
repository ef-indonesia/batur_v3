package com.traceon.batur.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.traceon.batur.data.db.*
import com.traceon.batur.data.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalRepository @Inject constructor(
    baselineDao: BaselineDao,
    komoditasDao: KomoditasDao,
    lahanDao: LahanDao,
    petaniDao: PetaniDao,
    referralDao: ReferralDao,
    satuanDao: SatuanDao,
    visitDao: VisitDao,
    compositeDisposable: CompositeDisposable
) {
    private val baseline: BaselineDao = baselineDao
    private val komoditas: KomoditasDao = komoditasDao
    private val lahan: LahanDao = lahanDao
    private val petani: PetaniDao = petaniDao
    private val satuan: SatuanDao = satuanDao
    private val referral: ReferralDao = referralDao
    private val visit: VisitDao = visitDao
    private val com: CompositeDisposable = compositeDisposable

    fun getAllBaseline(): LiveData<List<Baseline>> {
        return LiveDataReactiveStreams.fromPublisher(
            baseline.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun getAllKomoditas(): LiveData<List<Komoditas>> {
        return LiveDataReactiveStreams.fromPublisher(
            komoditas.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun getAllLahan(): LiveData<List<Lahan>> {
        return LiveDataReactiveStreams.fromPublisher(
            lahan.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun getAllPetani(): LiveData<List<Petani>> {
        return LiveDataReactiveStreams.fromPublisher(
            petani.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun getAllSatuan(): LiveData<List<Satuan>> {
        return LiveDataReactiveStreams.fromPublisher(
            satuan.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun getAllVisit(): LiveData<List<Visit>> {
        return LiveDataReactiveStreams.fromPublisher(
            visit.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
        )
    }

    fun updateBaseline(base: Baseline) {
        com.add(Observable.fromCallable { baseline.update(base) }
            .subscribeOn(Schedulers.computation())
            .subscribe())
    }
}