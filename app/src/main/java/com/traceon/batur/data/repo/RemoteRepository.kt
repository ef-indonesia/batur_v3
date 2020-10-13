package com.traceon.batur.data.repo

import com.traceon.batur.data.model.Petani
import com.traceon.batur.data.network.NetworkConfig
import com.traceon.batur.data.response.*
import io.reactivex.Observable
import retrofit2.Call

class RemoteRepository {
    private val api = NetworkConfig.getInstan()
    fun login(username: String, password: String): Observable<ResponseLogin> {
        return api.login(username, password)
    }

    fun ubahPassword(
        database: String,
        id: String,
        password: String,
        password_baru: String
    ): Call<ResponseUpdate> {
        return api.ubah_password(database, id, password, password_baru)
    }

    fun getDesa(
        database: String,
        area_id: String,
        staff_id: String
    ): Call<List<ResponseDesa>> {
        return api.get_desa(database, area_id, staff_id)
    }

    fun getArea(
        database: String,
        mu_id: String,
        staff_id: String
    ): Call<List<ResponseArea>> {
        return api.get_area(database, mu_id, staff_id)
    }

    fun getManagementUnit(
        database: String,
        staff_id: String
    ): Call<List<ResponseManagementUnit>> {
        return api.get_manajemen_unit(database, staff_id)
    }

    fun getPetani(
        database: String,
        desa_id: String,
        petani_id: String
    ): Call<List<Petani>> {
        return api.get_petani(database, desa_id, petani_id)
    }

    fun getLahan(
        database: String, petani_id: String, desa_id: String, kode: String
    ): Call<List<ResponseLahan>> {
        return api.get_lahan(database, petani_id, desa_id, kode)
    }

    fun getJenisKomoditas(
        database: String?,
        sub_kategori_komoditas_id: String?
    ): Call<List<ResponseJenisKomoditas>> {
        return api.get_komoditas(database, sub_kategori_komoditas_id)
    }

    fun getDeleteImage(database: String, file_path: String): Call<List<ResponseUpdate>> {
        return api.delete_image(database, file_path)
    }

    fun getProfile(
        database: String,
        table_id: String,
        tim_medis_id: String,
        user_id: String,
        token: String
    ): Call<List<ResponseProfile>> {
        return api.request_profil(database, table_id, tim_medis_id, user_id, token)
    }

    fun getPrioritasVisit(database: String, desa_id: String): Call<ResponsePrioritasVisit> {
        return api.get_prioritas_visit(database, desa_id)
    }

    fun getIncidentalVisit(database: String, kode: String): Call<ResponseIncidentalVisit> {
        return api.get_incidental_visit(database, kode)
    }
}