package com.example.musicplayer

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit

data class Music(val id: String, val title: String, val album: String, val artist: String, val duration: Long = 0, val path:String,
                 val artUri: String)

fun formatduration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration,
        TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%2d:%2d", minutes, seconds)
}

fun getImgart(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}
fun setsongposition(increment : Boolean) {
    if (increment){
        if (Songs_run.songsrunplayer.size -1 == Songs_run.songsposition)
            Songs_run.songsposition = 0
        else
            ++Songs_run.songsposition
    }
    else
    {
        if (0 == Songs_run.songsposition)
            Songs_run.songsposition = Songs_run.songsrunplayer.size -1
        else
            --Songs_run.songsposition
    }
}