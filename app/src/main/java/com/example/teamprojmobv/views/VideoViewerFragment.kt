package com.example.teamprojmobv.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.teamprojmobv.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_video_viewer.*


class VideoViewerFragment : Fragment() {

    private var mPlayer : SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_profile.setOnClickListener {
            view.findNavController().navigate(R.id.action_videoViewerFragment_to_profileFragment)
        }

        button_camera.setOnClickListener {
            view.findNavController().navigate(R.id.action_videoViewerFragment_to_cameraFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUi()
        if(Util.SDK_INT < 24 || mPlayer == null){
            initPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if(Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {

    }

    private fun initPlayer(){
        mPlayer = SimpleExoPlayer.Builder(requireContext(), ).build()
        player_view.player = mPlayer

        val mediaItem = MediaItem.Builder().setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4").build()
        mPlayer!!.addMediaItem(mediaItem)
        mPlayer!!.prepare()
//        mPlayer!!.play()   // autoplay

    }

    private fun releasePlayer(){
        if(mPlayer == null){
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }
}