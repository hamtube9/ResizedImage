package com.h.resizedimage

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.h.resizedimage.mvp.UploadImageResponse
import com.h.resizedimage.mvp.presenter.UploadImagePresenter
import com.h.resizedimage.mvp.view.UploadImageViewPresenter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity(), UploadImageViewPresenter {
    lateinit var presenter: UploadImagePresenter
    private lateinit var builder: MultipartBody.Builder

    private val token = "Bearer CMfqypJoyUxqo6qkF0vI"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = UploadImagePresenter()
        presenter.attachView(this)


        selectImage.setOnClickListener {
            CropImage.startPickImageActivity(this)
        }

        selectVideo.setOnClickListener {
            startActivity(Intent(this,VideoActivity::class.java))
        }
    }

    private fun cropImage(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }

    override fun uploadImageSuccess(response: UploadImageResponse) {

       val baseUrl = "https://hp102group.vn/"
        Glide.with(this@MainActivity).load(baseUrl+response.files[0]).into(cropImageView)
        Toast.makeText(this,
            "Upload success", Toast.LENGTH_SHORT).show()

    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data!!)
            cropImage(imageUri)
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)
                    Log.d("compress", "bitmap : " + bitmap.byteCount.toString())
                    val bitmapCompress = ImageCompression.getThumbnail(result.uri, this)
                    //cropImageView.setImageBitmap(bitmapCompress)

                    uploadImage(bitmapToFile(bitmapCompress!!))

                    Log.d("compress", "bitmapCompress : " + bitmapCompress!!.byteCount.toString())
                    Log.d(
                        "compressbitmaptofile",
                        "bitmap to file : " + bitmapToFile(bitmapCompress!!).path
                    )


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

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


    private fun uploadImage(uri: Uri) {
        builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        try {
            val file = File(uri.path)
            builder.addFormDataPart(
                "file[]",
                file.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            )
          val request = builder.build()
            presenter.uploadImageDetail(request, token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}

