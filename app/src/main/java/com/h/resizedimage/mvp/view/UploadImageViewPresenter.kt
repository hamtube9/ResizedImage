package com.h.resizedimage.mvp.view

import com.h.resizedimage.mvp.UploadImageResponse
import com.h.resizedimage.mvp.View

interface UploadImageViewPresenter : View{
    fun uploadImageSuccess(response: UploadImageResponse)
}