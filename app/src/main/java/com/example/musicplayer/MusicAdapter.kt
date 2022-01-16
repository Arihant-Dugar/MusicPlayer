package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.MusicViewInMainActivityBinding

class MusicAdapter(private val context:Context, private val musicList: ArrayList<Music>) : RecyclerView.Adapter<MusicAdapter.MyHolder>(){
    class MyHolder(binding: MusicViewInMainActivityBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.songNameMv
        val album = binding.songAlbumMv
        val image = binding.imagemv
        val duration = binding.SongDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MyHolder {
        return MyHolder(MusicViewInMainActivityBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MusicAdapter.MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatduration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.music_app_icon_round).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener {
            val intent = Intent(context,Songs_run::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","MusicAdapter")
            ContextCompat.startActivity(context,intent,null)

        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}