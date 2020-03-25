package com.h.resizedimage.thumnail

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.h.resizedimage.R
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        val intent = intent

        val uri: Uri = intent.getParcelableExtra("uri")


        videoView.setVideoURI(uri)
        videoView.setMediaController(MediaController(this))
//
//        val file = FileUtils.getFile(this,uri)
//        val request : RequestBody = file.asRequestBody("video/*".toMediaTypeOrNull())
//        val multipartBody = MultipartBody.Part.createFormData("upload",file.name,request)
//        presenter.uploadVideoDetail(multipartBody,token)


    }


}
