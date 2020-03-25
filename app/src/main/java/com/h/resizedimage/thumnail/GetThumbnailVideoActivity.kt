package com.h.resizedimage.thumnail

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.h.resizedimage.R
import kotlinx.android.synthetic.main.activity_get_thumbnail_video.*


class GetThumbnailVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_thumbnail_video)
        val intent = intent

   // val uri: Uri = intent.getParcelableExtra("uri")
        val url = intent.getStringExtra("uri")
        val uri = Uri.parse(url)
        Log.d("uriIntent",uri.toString())
        val player = SimpleExoPlayer.Builder(this).build()
        player_view.player = player

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "haibilmao")
        )

        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

        player.prepare(videoSource)
        player.playWhenReady = true
        player.prepare(videoSource)
      //  controls.player = player

        player_view.controllerHideOnTouch = true
        player_view.requestFocus()




    }

}
