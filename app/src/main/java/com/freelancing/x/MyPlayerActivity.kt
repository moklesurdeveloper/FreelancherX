package com.freelancing.x

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.halilibo.bvpkotlin.BetterVideoPlayer
import com.halilibo.bvpkotlin.VideoCallback

class MyPlayerActivity : AppCompatActivity() {

    lateinit var bvp: BetterVideoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR



        bvp = findViewById(R.id.bvp)!!

        if (savedInstanceState == null) {
            bvp.setAutoPlay(true)
            bvp.setSource(Uri.parse(TEST_URL))

        }

        bvp.setHideControlsOnPlay(true)



        bvp.enableSwipeGestures(window)

        bvp.setCallback(object : VideoCallback {
            override fun onStarted(player: BetterVideoPlayer) {
                Log.i(TAG, "Started")
            }

            override fun onPaused(player: BetterVideoPlayer) {
                Log.i(TAG, "Paused")
            }

            override fun onPreparing(player: BetterVideoPlayer) {
                Log.i(TAG, "Preparing")
            }

            override fun onPrepared(player: BetterVideoPlayer) {
                Log.i(TAG, "Prepared")
            }

            override fun onBuffering(percent: Int) {
                Log.i(TAG, "Buffering $percent")
            }

            override fun onError(player: BetterVideoPlayer, e: Exception) {
                Log.i(TAG, "Error " +e.message)
            }

            override fun onCompletion(player: BetterVideoPlayer) {
                Log.i(TAG, "Completed")
            }

            override fun onToggleControls(player: BetterVideoPlayer, isShowing: Boolean) {

            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportActionBar?.show()
        }
    }

    public override fun onPause() {
        bvp.pause()
        super.onPause()
    }


    companion object {  const val TAG = "MainActivity_x"
        const val TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    }
}