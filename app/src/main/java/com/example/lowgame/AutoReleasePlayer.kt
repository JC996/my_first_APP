package com.example.lowgame

import android.content.Context
import android.media.MediaPlayer

class AutoReleasePlayer(context : Context, id : Int){
    private var mediaPlayer = MediaPlayer.create(context,id)
    private var isReleased = false
    init {
        mediaPlayer.setOnCompletionListener {
            isReleased = true
            it.release()
        }
        mediaPlayer.start()
    }

    fun pause(){
        if(!isReleased)
            mediaPlayer.pause()
    }

    fun start(){
        if(!isReleased)
            mediaPlayer.start()
    }

    fun release(){
        if(!isReleased){
            isReleased = true
            mediaPlayer.release()
        }
    }
}