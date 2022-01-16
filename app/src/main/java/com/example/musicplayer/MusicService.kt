package com.example.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var myBinder = MyBinder()  // mybinder is an instance of IBinder
    var mediaPlayer : MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat


    override fun onBind(p0: Intent?): IBinder{
        mediaSession = MediaSessionCompat(this,"My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }
    fun showNotification(playpausebtn : Int){

        val prevIntent = Intent(this,BroadcastNotificationReceiver::class.java).setAction(ApplicationClass.PERVIOUS)
        val prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(this,BroadcastNotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playpendingIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(this,BroadcastNotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(this,BroadcastNotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitpendingIntent = PendingIntent.getBroadcast(this,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val Imgart = getImgart(Songs_run.songsrunplayer[Songs_run.songsposition].path)
        val image = if(Imgart != null){
            BitmapFactory.decodeByteArray(Imgart,0,Imgart.size)
        }else{
            BitmapFactory.decodeResource(resources, R.mipmap.music_app_icon_round)
        }

        val notification = NotificationCompat.Builder(this,ApplicationClass.CHANNEL_ID)
            .setContentTitle(Songs_run.songsrunplayer[Songs_run.songsposition].title)
            .setContentText(Songs_run.songsrunplayer[Songs_run.songsposition].artist)
            .setSmallIcon(R.drawable.music_icon)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.before_btn,"previous",prevpendingIntent)
            .addAction(playpausebtn,"play",playpendingIntent)
            .addAction(R.drawable.next_bt,"next",nextpendingIntent)
            .addAction(R.drawable.exit_icon,"exit",exitpendingIntent)
            .build()

        startForeground(7,notification)
    }
     fun createmediaplayer() {
        try {
            if (Songs_run.musicService!!.mediaPlayer == null) {
                Songs_run.musicService!!.mediaPlayer = MediaPlayer()
            } else {
                Songs_run.musicService!!.mediaPlayer!!.reset()
                Songs_run.musicService!!.mediaPlayer!!.setDataSource(Songs_run.songsrunplayer[Songs_run.songsposition].path)
                Songs_run.musicService!!.mediaPlayer!!.prepare()
                Songs_run.binding.playPauseButton.setIconResource(R.drawable.pause_now)
                Songs_run.musicService!!.showNotification(R.drawable.pause_now)
                Songs_run.binding.startTime.text = formatduration(Songs_run.musicService!!.mediaPlayer!!.currentPosition.toLong())
                Songs_run.binding.endTime.text = formatduration(Songs_run.musicService!!.mediaPlayer!!.duration.toLong())
                Songs_run.binding.seekBar.progress = 0
                Songs_run.binding.seekBar.max = Songs_run.musicService!!.mediaPlayer!!.duration
            }
        } catch (e: Exception) {
            return
        }
    }
}