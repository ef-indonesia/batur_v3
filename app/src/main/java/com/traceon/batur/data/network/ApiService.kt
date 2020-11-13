package com.traceon.batur.data.network

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
    ): Observable<List<ResponseManagementUnit>>

    @GET("get_area?lim=1")
    fun get_area(
        @Query("database") database: String?,
        @Query("manajemen_unit_ID") manajemen_unit_ID: String?,
        @Query("staff_ID") staff_ID: String?
    ): Observable<List<ResponseArea>>

    @GET("get_desa?lim=1")
    fun get_desa(
        @Query("database") database: String?,
        @Query("area_ID") area_ID: String?,
        @Query("staff_ID") staff_ID: String?
    ): Observable<List<ResponseDesa>>

    @GET("get_kode_referal?lim=1")
    fun get_kode_referal(
        @Query("database") database: String?,
        @Query("staff_ID") staff_ID: String?
    ): Observable<ResponseReferral>

    @GET("get_petani_new/?lim=1")
    fun get_petani_new(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?,
        @Query("ID") petani_ID: String?
    ): Observable<ResponsePetani>


    @GET("get_lahan_new/?lim=1")
    fun get_lahan_new(
        @Query("database") database: String?,
        @Query("petani_ID") petani_ID: String?
    ): Observable<ResponseLahan>

    @GET("get_baseline_in/?lim=1")
    fun get_baseline_in(
        @Query("database") database: String?,
        @Query("petani_ID") petani_ID: String?
    ): Observable<ResponseBaseline>

    @GET("get_komoditas?lim=1")
    fun get_komoditas(
        @Query("database") database: String?,
        @Query("sub_kategori_komoditas_ID") sub_kategori_komoditas_ID: String?
    ): Observable<ResponseKomoditas>

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
    ): Observable<ResponsePrioritasVisit>

    @GET("get_prioritas_visit_new")
    fun get_prioritas_visit_new(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?,
        @Query("frequent") frequent: String?
    ): Observable<ResponsePrioritasVisit>

    @GET("get_incidental_visit")
    fun get_incidental_visit(
        @Query("database") database: String?,
        @Query("kode") kode: String?
    ): Observable<ResponseIncidentalVisit>

    @GET("get_komoditas_baseline")
    fun get_komoditas_baseline(
        @Query("database") database: String?,
        @Query("group_komoditas_ID") idKomoditas: String?,
        @Query("lahan_ID") idSubLahan: String?,
        @Query("kode") kode: String?,
    ): Observable<ResponseKomoditasVisit>

    @GET("get_petani_program_visit")
    fun get_petani_program_visit(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?
    ): Observable<ResponseChart>

    @GET("get_check_point")
    fun get_check_point(
        @Query("database") database: String?,
        @Query("check_area_ID") check_area_ID: String?
    ): Observable<ResponseCheckPoint>

    @GET("get_check_area")
    fun get_check_area(
        @Query("database") database: String?,
        @Query("group_komoditas_ID") group_komoditas_ID: String?,
        @Query("fase_ID") fase_ID: String?
    ): Observable<ResponseCheckArea>

    @GET("get_fase")
    fun get_fase(
        @Query("database") database: String?,
        @Query("group_komoditas_ID") group_komoditas_ID: String?
    ): Observable<ResponseFase>

    @GET("get_komoditas_baru")
    fun get_komoditas_baru(
        @Query("database") database: String?,
        @Query("group_komoditas_ID") group_komoditas_ID: String?
    ): Observable<ResponseKomoditasBaru>

    @GET("get_program_baru")
    fun get_program_baru(
        @Query("database") database: String?,
        @Query("program_ID") program_ID: String?
    ): Observable<ResponseProgramBaru>

    @GET("get_program")
    fun get_program(
        @Query("database") database: String?
    ): Observable<ResponseProgram>

    @GET("get_status")
    fun get_status(
        @Query("database") database: String?
    ): Observable<ResponseStatus>

    @GET("show_grafik_petani_program_visit")
    fun show_grafik_petani_program_visit(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?
    ): Observable<ResponseDashboardVisit>

    @GET("get_table_frequent_visit")
    fun get_table_frequent_visit(
        @Query("database") database: String?,
        @Query("desa_ID") desa_ID: String?
    ): Observable<ResponseFrequent>
}