package com.example.teamprojmobv

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val change = findViewById<Button>(R.id.show_video_view)

        change.setOnClickListener { showVideoFragment() }
    }

    private fun showVideoFragment(){
        val transaction = manager.beginTransaction()
        val fragment = VideoFragment()
        transaction.replace(R.id.video_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}