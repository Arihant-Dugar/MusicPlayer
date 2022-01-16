package com.example.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.ActivitySongsRunBinding

class Songs_run : AppCompatActivity() , ServiceConnection {


    companion object {
        var songsrunplayer = ArrayList<Music>()
        var songsposition: Int = 0
        var isplaying : Boolean = false
        var musicService: MusicService? = null
        lateinit var binding: ActivitySongsRunBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivitySongsRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // for starting the service
        var intent = Intent(this,MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        initialiazelayout()
        binding.playPauseButton.setOnClickListener {
            if (isplaying)
                pausemusic()
            else
                playmusic()
        }
        binding.previousButton.setOnClickListener {
            previoussong(increment = false)

        }
        binding.nextButton.setOnClickListener {
            previoussong(increment = true)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbaar: SeekBar?, progress: Int, fromuser: Boolean) {
                if(fromuser) musicService!!.mediaPlayer!!.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
    }

    private fun songslayout() {
        Glide.with(this)
            .load(songsrunplayer[songsposition].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.music_app_icon_round).centerCrop())
            .into(binding.songImage)
        binding.songName.text = songsrunplayer[songsposition].title
    }

    private fun createmediaplayer() {
        try {
            if (musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer()
            } else {
                musicService!!.mediaPlayer!!.reset()
                musicService!!.mediaPlayer!!.setDataSource(Songs_run.songsrunplayer[Songs_run.songsposition].path)
                musicService!!.mediaPlayer!!.prepare()
                musicService!!.mediaPlayer!!.start()
                isplaying = true
                binding.playPauseButton.setIconResource(R.drawable.pause_now)
                musicService!!.showNotification(R.drawable.pause_now)
                binding.startTime.text = formatduration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.endTime.text = formatduration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.seekBar.progress = 0
                binding.seekBar.max = musicService!!.mediaPlayer!!.duration
            }
        } catch (e: Exception) {
            return
        }
    }
    private fun initialiazelayout(){
        songsposition = intent.getIntExtra("name", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                songsrunplayer = ArrayList()
                songsrunplayer.addAll(MainActivity.MusiclistMA)
                songslayout()
                createmediaplayer()
            }
            "MainActivity" -> {
                songsrunplayer = ArrayList()
                songsrunplayer.addAll(MainActivity.MusiclistMA)
                songsrunplayer.shuffle()
                songslayout()
                createmediaplayer()
            }
        }
    }
    private fun playmusic(){
        binding.playPauseButton.setIconResource(R.drawable.pause_now)
        musicService!!.showNotification(R.drawable.pause_now)
        isplaying = true
        musicService!!.mediaPlayer!!.start()
    }
    private fun pausemusic(){
        binding.playPauseButton.setIconResource(R.drawable.play_now)
        musicService!!.showNotification(R.drawable.play_now)
        isplaying = false
        musicService!!.mediaPlayer!!.pause()
    }
    private  fun  previoussong(increment: Boolean){
        if(increment){
            setsongposition(increment = true)
            songslayout()
        }
        else{
            setsongposition(increment = false)
            songslayout()
        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()
        createmediaplayer()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }
}