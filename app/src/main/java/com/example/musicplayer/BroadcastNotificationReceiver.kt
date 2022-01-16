package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class BroadcastNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
       when(p1?.action){
           ApplicationClass.PERVIOUS -> prevnextsong(increment = false, context = p0!!)
           ApplicationClass.PLAY -> if(Songs_run.isplaying){
               pausemusic()
           }else{
               playmusic()
           }
           ApplicationClass.NEXT -> prevnextsong(increment = true, context = p0!!)
           ApplicationClass.EXIT -> {
               Songs_run.musicService!!.stopForeground(true)
               Songs_run.musicService = null
               exitProcess(1)
           }
       }
    }
    private fun playmusic(){
        Songs_run.isplaying = true
        Songs_run.musicService!!.mediaPlayer!!.start()
        Songs_run.musicService!!.showNotification(R.drawable.pause_now)
        Songs_run.binding.playPauseButton.setIconResource(R.drawable.pause_now)
    }
    private  fun pausemusic(){
        Songs_run.isplaying = false
        Songs_run.musicService!!.mediaPlayer!!.pause()
        Songs_run.musicService!!.showNotification(R.drawable.play_now)
        Songs_run.binding.playPauseButton.setIconResource(R.drawable.play_now)
    }
    private fun prevnextsong(increment : Boolean, context: Context){
        setsongposition(increment = increment)
        Songs_run.musicService!!.createmediaplayer()
        Glide.with(context)
            .load(Songs_run.songsrunplayer[Songs_run.songsposition].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.music_app_icon_round).centerCrop())
            .into(Songs_run.binding.songImage)
        Songs_run.binding.songName.text = Songs_run.songsrunplayer[Songs_run.songsposition].title
        playmusic()
    }
}