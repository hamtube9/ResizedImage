package com.h.resizedimage

import android.annotation.TargetApi
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vincent.videocompressor.VideoCompress
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VideoActivity : AppCompatActivity() {
    private val REQUEST_FOR_VIDEO_FILE = 1000


    private val outputDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath

    private val inputPath: String? = null
    private val outputPath: String? = null



    private var startTime: Long = 0
    private  var endTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        initView()
    }

    private fun initView() {

        btn_select.setOnClickListener {
            val intent = Intent()
            /* 开启Pictures画面Type设定为image */ //intent.setType("video/*;image/*");
            //intent.setType("audio/*"); //选择音频
            intent.type = "video/*" //选择视频 （mp4 3gp 是android支持的视频格式）
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, this.REQUEST_FOR_VIDEO_FILE)
        }

        btn_compress.setOnClickListener {
            val destPath =
                tv_output!!.text.toString() + File.separator + "VID_" + SimpleDateFormat(
                    "yyyyMMdd_HHmmss",
                    getLocale()
                ).format(Date()) + ".mp4"
            VideoCompress.compressVideoLow(
                tv_input!!.text.toString(),
                destPath,
                object : VideoCompress.CompressListener {
                    override fun onStart() {
                        tv_indicator!!.text = ("Compressing..." + "\n"
                                + "Start at: " + SimpleDateFormat(
                            "HH:mm:ss",
                            getLocale()
                        ).format(Date()))
                        pb_compress!!.visibility = View.VISIBLE
                        startTime = System.currentTimeMillis()
                        Util.writeFile(
                            this@VideoActivity,
                            "Start at: " + SimpleDateFormat(
                                "HH:mm:ss",
                                getLocale()
                            ).format(Date()) + "\n"
                        )
                    }

                    override fun onSuccess() {
                        val previous = tv_indicator!!.text.toString()
                        tv_indicator!!.text = (previous + "\n"
                                + "Compress Success!" + "\n"
                                + "End at: " + SimpleDateFormat(
                            "HH:mm:ss",
                            getLocale()
                        ).format(Date()))
                        pb_compress!!.visibility = View.INVISIBLE
                        endTime = System.currentTimeMillis()
                        Util.writeFile(
                            this@VideoActivity,
                            "End at: " + SimpleDateFormat(
                                "HH:mm:ss",
                                getLocale()
                            ).format(Date()) + "\n"
                        )
                        Util.writeFile(
                            this@VideoActivity,
                            "Total: " + (endTime - startTime) / 1000 + "s" + "\n"
                        )
                        Util.writeFile(this@VideoActivity)
                    }

                    override fun onFail() {
                        tv_indicator!!.text = "Compress Failed!"
                        pb_compress!!.visibility = View.INVISIBLE
                        endTime = System.currentTimeMillis()
                        Util.writeFile(
                            this@VideoActivity,
                            "Failed Compress!!!" + SimpleDateFormat(
                                "HH:mm:ss",
                                getLocale()
                            ).format(Date())
                        )
                    }

                    override fun onProgress(percent: Float) {
                        tv_progress!!.text = "$percent%"
                    }
                })
        }

        tv_output!!.text = outputDir

    }

    private fun getLocale(): Locale? {
        val config = resources.configuration
        var sysLocale: Locale? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = this.getSystemLocale(config)
        } else {
            sysLocale = this.getSystemLocaleLegacy(config)
        }
        return sysLocale
    }

    fun getSystemLocaleLegacy(config: Configuration): Locale? {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale? {
        return config.locales[0]
    }
}
