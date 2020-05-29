package com.lauwba.uploaddatacameragallery.network

import com.lauwba.uploaddatacameragallery.ResponseUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    //Multipart harus POST gak bisa pake GET
    @Multipart
    @POST("Wisata/addWisata")
    fun doUpload(
        @Part foto : MultipartBody.Part,
        @Part("nama_wisata") namawisata: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("latitude") lat: RequestBody,
        @Part("longitude") lon: RequestBody,
        @Part("notelp") notelp: RequestBody
    ): retrofit2.Call<ResponseUpload>

}