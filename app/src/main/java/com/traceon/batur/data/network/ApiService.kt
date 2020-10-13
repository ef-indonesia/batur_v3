package com.traceon.batur.data.network

import com.traceon.batur.data.model.Petani
import com.traceon.batur.data.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<ResponseLogin>

    @POST("ubah_password")
    fun ubah_password(
        @Query("database") database: String,
        @Query("ID") ID: String,
        @Query("password") password: String,
        @Query("password_baru") password_baru: String
    ): Call<ResponseUpdate>

    @GET("get_manajemen_unit?lim=1")
    fun get_manajemen_unit(
        @Query("database") database: String?,
        @Query("staff_ID") staff_ID: String?
    ): Call<List<ResponseManagementUnit>>

    @GET("get_area?lim=1")
    fun get_area(
        @Query("database") database: String?,
        @Query("manajemen_unit_ID") manajemen_unit_ID: String?,
        @Query("staff_ID") staff_ID: String?
    ): Call<List<ResponseArea>>

    @GET("get_desa?lim=1")
    fun get_desa(
        @Query("database") database: String?,
        @Query("area_ID") area_ID: String?,
        @Query("staff_ID") staff_ID: String?
    ): Call<List<ResponseDesa>>

    @GET("get_petani/?lim=1")
    fun get_petani(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?,
        @Query("ID") petani_ID: String?
    ): Call<List<Petani>>

    @GET("get_lahan/?lim=1")
    fun get_lahan(
        @Query("database") database: String?,
        @Query("petani_ID") petani_ID: String?,
        @Query("desa_ID") desa_ID: String?,
        @Query("kode") kode: String?
    ): Call<List<ResponseLahan>>

    @GET("get_komoditas?lim=1")
    fun get_komoditas(
        @Query("database") database: String?,
        @Query("sub_kategori_komoditas_ID") sub_kategori_komoditas_ID: String?
    ): Call<List<ResponseJenisKomoditas>>

    @GET("delete_image")
    fun delete_image(
        @Query("database") database: String?,
        @Query("file_path") file_path: String?
    ): Call<List<ResponseUpdate>>

    @GET("profile/?&client_ID=1&jenis_order=online")
    fun request_profil(
        @Query("table_name") table_name: String?,
        @Query("table_ID") table_ID: String?,
        @Query("tim_medis_ID") tim_medis_ID: String?,
        @Query("user_ID") user_ID: String?,
        @Query("token") token: String?
    ): Call<List<ResponseProfile>>

    @Multipart
    @POST("simpan_petani")
    fun simpan_petani(
        @Part file: MultipartBody.Part?,
        @PartMap data: Map<String, RequestBody>
    ): Call<List<ResponseUpdate>>

    @GET("get_prioritas_visit")
    fun get_prioritas_visit(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?
    ): Call<ResponsePrioritasVisit>

    @GET("get_incidental_visit")
    fun get_incidental_visit(
        @Query("database") database: String?,
        @Query("kode") kode: String?
    ): Call<ResponseIncidentalVisit>

    @GET("get_komoditas_baseline")
    fun get_komoditas_baseline(
        @Query("database") database: String?,
        @Query("group_komoditas_ID") idKomoditas: String?,
        @Query("lahan_ID") idSubLahan: String?,
        @Query("kode") kode: String?,
    ): Call<ResponseKomoditasVisit>
}