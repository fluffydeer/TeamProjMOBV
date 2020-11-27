package com.example.teamprojmobv.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.teamprojmobv.R


class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_profile.setOnClickListener {
            view.findNavController().navigate(R.id.action_cameraFragment_to_profileFragment)
        }

        button_video_viewer.setOnClickListener {
            view.findNavController().navigate(R.id.action_cameraFragment_to_videoViewerFragment)
        }
    }*/
}