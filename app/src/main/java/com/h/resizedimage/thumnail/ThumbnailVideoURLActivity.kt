package com.h.resizedimage.thumnail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.h.resizedimage.R
import kotlinx.android.synthetic.main.activity_thumbnail_video_url.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.HashMap

class ThumbnailVideoURLActivity : AppCompatActivity() {
    var bitmap: Bitmap? = null
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    var url = ""
    var uri: Uri? = null
    val REQUEST_FOR_VIDEO_FILE: Int = 121

    val MY_PERMISSION_REQUEST = 89
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumbnail_video_url)


        btnUpload.setOnClickListener {
          val bitmapUri =  getVideoFrame(uri!!, this)
            imgView.setImageBitmap(bitmapUri)
        }

        btnCheck.setOnClickListener {


            url = edtURL.text.toString()
            try {
                bitmap =
                    retriveVideoFrameFromVideo(url)
                Log.d("bitmap", bitmap.toString())
                if (bitmap != null) {
                    imgView.setImageBitmap(bitmap)
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
//
//            val intent = Intent()
//            /* 开启Pictures画面Type设定为image */ //intent.setType("video/*;image/*");
//            //intent.setType("audio/*"); //选择音频
//            intent.type = "video/*" //选择视频 （mp4 3gp 是android支持的视频格式）
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(intent, REQUEST_FOR_VIDEO_FILE)
        }

        btnSetImage.setOnClickListener {

            if ((ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED)
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSION_REQUEST
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSION_REQUEST
                    )
                }
            } else {
                val intent = Intent()
                /* 开启Pictures画面Type设定为image */ //intent.setType("video/*;image/*");
                //intent.setType("audio/*"); //选择音频
                intent.type = "video/*" //选择视频 （mp4 3gp 是android支持的视频格式）
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 212)
            }

//
//

        }

        imgView.setOnClickListener {
            val intent = Intent(this, VideoViewActivity::class.java)
          //  val uri = bitmapToFile(bitmap!!)
          //  Log.d("uriData", uri.toString())
            intent.putExtra("uri", uri)
            startActivity(intent)

        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_FOR_VIDEO_FILE) {
//            if (resultCode == Activity.RESULT_OK) {
//                val data = data!!.data
//                if (data != null) {
//                    val intent = Intent(
//                        this@ThumbnailVideoURLActivity,
//                        GetThumbnailVideoActivity::class.java
//                    )
//                    intent.putExtra("uri", data!!)
//                    startActivity(intent)
//                }
//            }
//        }
        if (requestCode == 212) {
            if (resultCode == Activity.RESULT_OK) {

                val data = data!!.data
                if (data != null) {
                    uri = data
                    Toast.makeText(this, data.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String): Bitmap {
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever!!.setDataSource(videoPath, HashMap<String, String>())
            // mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever!!.frameAtTime

        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever!!.release()
            }
        }
        return bitmap!!
    }

    private fun getVideoFrame(uri: Uri, context: Context): Bitmap {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        return retriever.frameAtTime
    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }





}
