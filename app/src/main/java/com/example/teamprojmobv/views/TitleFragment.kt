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
import com.example.teamprojmobv.data.util.Injection


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

        //throw RuntimeException("Test crash")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseViewModel.successRes.observe(viewLifecycleOwner)
        {
            it?.let {
                if (it == true) {
                    databaseViewModel.successRes.value = null
                    view.findNavController()
                        .navigate(R.id.action_titleFragment_to_videoViewerFragment)
                    //databaseViewModel.successRes.removeObservers(viewLifecycleOwner);
                } else {
                    Toast.makeText(activity, "Incorrect login data!", Toast.LENGTH_SHORT).show();
                }
            }
        }

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
            databaseViewModel.login()
        }

        binding.textViewLOG.setOnClickListener{
            view.findNavController().navigate(R.id.action_titleFragment_to_registerFragment)
        }
    }
}