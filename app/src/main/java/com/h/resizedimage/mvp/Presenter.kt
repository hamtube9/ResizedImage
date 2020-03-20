package com.h.resizedimage.mvp

interface Presenter {
    fun attachView(view: View)
    fun dispose()
}