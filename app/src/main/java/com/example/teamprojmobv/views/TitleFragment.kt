package com.example.teamprojmobv.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.teamprojmobv.R
import com.example.teamprojmobv.views.viewModels.HomeViewModel
import com.example.teamprojmobv.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentTitleBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false
        )
        binding.lifecycleOwner = this
        //binding.model = homeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonLoginLOG.setOnClickListener {
            //Login()
            view.findNavController().navigate(R.id.action_titleFragment_to_videoViewerFragment)

        }

        binding.textViewLOG.setOnClickListener{
            view.findNavController().navigate(R.id.action_titleFragment_to_registerFragment)
        }
    }
/*
    private fun Login() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.mcomputing.eu/")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("action", "login")
        jsonObject.put("apikey", "yS9zD3dI4uR2aK0cY9cS5pT6tK2rZ6")
        jsonObject.put("username", editUserNameLOG)
        jsonObject.put("password", editTextPasswordLOG)

        /*
        jsonObject.put("action", "register")
        jsonObject.put("apikey", "yS9zD3dI4uR2aK0cY9cS5pT6tK2rZ6")
        jsonObject.put("email", "john3@doe.com")
        jsonObject.put("username", "test12")
        jsonObject.put("password", "oV3aK9iB5")*/

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.loginUser(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)
                    val user = gson.fromJson(prettyJson, LoggedUser::class.java)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }*/
}