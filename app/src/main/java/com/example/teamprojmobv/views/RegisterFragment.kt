package com.example.teamprojmobv.views

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.teamprojmobv.R
import com.example.teamprojmobv.databinding.FragmentRegisterBinding
import com.example.teamprojmobv.views.viewModels.DatabaseViewModel
import com.example.teamprojmobv.Data.util.Injection

class RegisterFragment : Fragment() {
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        binding.lifecycleOwner = this
        databaseViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
            .get(DatabaseViewModel::class.java)
        binding.model = databaseViewModel

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
                        .navigate(R.id.action_registerFragment_to_videoViewerFragment)
                    //databaseViewModel.successRes.removeObservers(viewLifecycleOwner);
                } else {
                    Toast.makeText(getActivity(), "Incorrect register data or user exists!", Toast.LENGTH_SHORT)
                        .show();
                }
            }
        }

            binding.buttonRegisterREG.setOnClickListener {
                val username = databaseViewModel.username.value
                val email = databaseViewModel.email.value
                val password = databaseViewModel.password.value

                if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches())

                // if(email.isNullOrBlank() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.editEmailREG.error = "Valid email required"
                    binding.editEmailREG.requestFocus()
                    return@setOnClickListener
                }

                if(username.isNullOrBlank())
                {
                    binding.editUserNameREG.error = "Username required"
                    binding.editUserNameREG.requestFocus()
                    return@setOnClickListener
                }

                // ak je username prazdny alebo null tak sa sem nedostane
                if(username.length < 4)
                {
                    binding.editUserNameREG.error = "Username requires at least 4 characters"
                    binding.editUserNameREG.requestFocus()
                    return@setOnClickListener
                }

                if(username.length > 32)
                {
                    binding.editUserNameREG.error = "Maximum username length is 32 characters"
                    binding.editUserNameREG.requestFocus()
                    return@setOnClickListener
                }

                if(password.isNullOrBlank())
                {
                    binding.editTextPasswordREG.error = "Password required"
                    binding.editTextPasswordREG.requestFocus()
                    return@setOnClickListener
                }

                if(password.length < 4)
                {
                    binding.editTextPasswordREG.error = "Password requires at least 4 characters"
                    binding.editTextPasswordREG.requestFocus()
                    return@setOnClickListener
                }

                if(password.length > 32)
                {
                    binding.editTextPasswordREG.error = "Maximum password length is 32 characters"
                    binding.editTextPasswordREG.requestFocus()
                    return@setOnClickListener
                }

                databaseViewModel.register()
        }
        binding.textViewLoginREG.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerFragment_to_titleFragment)
        }
    }
}