package com.h.resizedimage

import com.h.resizedimage.mvp.UploadImageResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {


    @POST("api/file/upload")
    fun uploadMultiImage(@Body file: RequestBody, @Header("Authorization") authorization: String) : Observable<UploadImageResponse>


    @POST("api/file/upload")
    fun uploadVideo(@Body file: MultipartBody.Part, @Header("Authorization") authorization: String) : Observable<UploadImageResponse>
}