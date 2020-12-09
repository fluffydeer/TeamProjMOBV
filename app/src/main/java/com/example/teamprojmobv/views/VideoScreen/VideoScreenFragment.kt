package com.example.teamprojmobv.views.VideoScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.room.DatabaseView
import com.example.teamprojmobv.R
import com.example.teamprojmobv.data.db.model.MediaItem
import com.example.teamprojmobv.data.util.Injection
import com.example.teamprojmobv.data.util.PlayerViewAdapter
import com.example.teamprojmobv.data.util.RecyclerViewScrollListener
import com.example.teamprojmobv.views.viewModels.DatabaseViewModel
//import com.example.teamprojmobv.views.viewModels.MediaViewModel
import kotlinx.android.synthetic.main.fragment_video_player.*


/**
 * A simple [Fragment] subclass.
 */
class VideoScreenFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: VideoRecyclerAdapter? = null
    private val modelList: ArrayList<MediaItem> = ArrayList<MediaItem>()

    // for handle scroll and get first visible item index
    private lateinit var scrollListener: RecyclerViewScrollListener


    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =
            inflater.inflate(R.layout.fragment_video_player, container, false)
        findViews(view)
        return view
    }

    private lateinit var model: DatabaseViewModel

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        // load data
//        val model: DatabaseViewModel by viewModels()

        model = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
            .get(DatabaseViewModel::class.java)
//        val zoznam = model.getVideos()
        val zoznam : ArrayList<MediaItem>? = model.getVideos()
        if (zoznam != null) {
            mAdapter?.updateList(zoznam)
        }
//        model.getMedia()?.observe(requireActivity(), Observer {
//            mAdapter?.updateList(arrayListOf(*it.toTypedArray()))
//        })

        button_camera2.setOnClickListener{
            view.findNavController().navigate(R.id.action_videoViewerFragment_to_cameraFragment)
        }
        button_profile2.setOnClickListener{
            view.findNavController().navigate(R.id.action_videoViewerFragment_to_profileFragment)
        }

    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
    }

    private fun setAdapter() {
        mAdapter = VideoRecyclerAdapter(requireActivity(), modelList)
        recyclerView!!.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(getActivity())
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView!!)

        scrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())
                // play just visible item
                if (index != -1)
                    PlayerViewAdapter.playIndexThenPausePreviousPlayer(index)
            }

        }
        recyclerView!!.addOnScrollListener(scrollListener)
        mAdapter!!.SetOnItemClickListener(object : VideoRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: MediaItem?) {
                PlayerViewAdapter.pauseResume()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }
}