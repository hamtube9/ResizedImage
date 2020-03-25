package com.h.resizedimage.mvp.presenter

import android.util.Log
import com.h.resizedimage.ApiUtil
import com.h.resizedimage.mvp.Presenter
import com.h.resizedimage.mvp.UploadImageResponse
import com.h.resizedimage.mvp.View
import com.h.resizedimage.mvp.view.UploadImageViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadImagePresenter : Presenter {

    private lateinit var viewPresenter: UploadImageViewPresenter
    private val composite = CompositeDisposable()

    fun uploadImageDetail(images: RequestBody, token:String){
        composite.add(
            ApiUtil.getAPIService().uploadMultiImage(images,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUploadImageSuccess){
                        t -> onUploadFileFail("Upload Image không thành công !",t)
                }
        )
    }

    fun uploadVideoDetail(images: MultipartBody.Part, token:String){
        composite.add(
            ApiUtil.getAPIService().uploadVideo(images,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUploadImageSuccess){ t -> onUploadFileFail("Upload Image không thành công !",t)
                }
        )
    }
    private fun onUploadImageSuccess(response : UploadImageResponse){
        if(response.success==1){
            viewPresenter.uploadImageSuccess(response)
        }
    }
    private fun onUploadFileFail(error:String,t:Throwable){
        viewPresenter.showError(error)
        Log.d("onUploadFileFail",t.localizedMessage)
    }

    override fun attachView(view: View) {
        viewPresenter = view as UploadImageViewPresenter
    }

    override fun dispose() {
        composite.dispose()
    }
}