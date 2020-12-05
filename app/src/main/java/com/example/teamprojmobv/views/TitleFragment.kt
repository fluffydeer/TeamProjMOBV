package com.example.teamprojmobv.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.teamprojmobv.R
import com.example.teamprojmobv.databinding.FragmentTitleBinding
import com.example.teamprojmobv.views.viewModels.DatabaseViewModel
import com.example.teamprojmobv.Data.util.Injection


class TitleFragment : Fragment() {
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false
        )
        binding.lifecycleOwner = this
        databaseViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
            .get(DatabaseViewModel::class.java)
        binding.model = databaseViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonLoginLOG.setOnClickListener {

            val username = databaseViewModel.username.value
            val password = databaseViewModel.password.value

            if(username.isNullOrBlank())
            {
                binding.editUserNameLOG.error = "Username required"
                binding.editUserNameLOG.requestFocus()
                return@setOnClickListener
            }

            if(password.isNullOrBlank())
            {
                binding.editTextPasswordLOG.error = "Password required"
                binding.editTextPasswordLOG.requestFocus()
                return@setOnClickListener
            }

            //Login()
            // register aj apikey dat ako konstanty, hashovat heslo
            databaseViewModel.login()



            databaseViewModel.actualUser.observe(viewLifecycleOwner)
            {
                if (it != null) {
                    if (!it.username.isNullOrEmpty()) {
                        view.findNavController()
                            .navigate(R.id.action_titleFragment_to_videoViewerFragment)
                    }
                    else{
                        Toast.makeText(getActivity(),"Incorrect login data!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Incorrect login data!",Toast.LENGTH_SHORT).show();
                }
            }
        }

        binding.textViewLOG.setOnClickListener{
            view.findNavController().navigate(R.id.action_titleFragment_to_registerFragment)
        }
    }
}