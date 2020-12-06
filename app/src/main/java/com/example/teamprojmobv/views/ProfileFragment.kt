package com.example.teamprojmobv.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teamprojmobv.Data.util.Injection
import com.example.teamprojmobv.R
import com.example.teamprojmobv.databinding.FragmentProfileBinding
import com.example.teamprojmobv.views.viewModels.DatabaseViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var binding: FragmentProfileBinding
    private var selectedImageUri: Uri? = null
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        databaseViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        ).get(DatabaseViewModel::class.java)

        return binding.root
    }

    //todo toto vsetko treba hodit do viemodelu ci?
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo = databaseViewModel.getUserInfo()
        binding.nickname = userInfo.username
        binding.mail = userInfo.email

        profileImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        imageEditIcon.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        buttonChangePwd.setOnClickListener{
            showChangePasswordViews()
        }

        buttonSavePasswordChange.setOnClickListener{
            //TODO tu by som mala spristupnovat local cache ale narychlo som to nevedela
            val pwd = databaseViewModel.getPassword()
            Log.i("ProfileFragemnt", pwd)

            if(editTextOldPassword.text.toString() != pwd){
                createToast("Incorrect old password")
            }else if(editTextNewPassword.text.toString() == "" || editTextConfirmPassword.text.toString() == ""){
                createToast("Fields cannot be empty")
            } else if(editTextOldPassword.text.toString() == editTextNewPassword.text.toString()){
                createToast("Old and new passwords are the same")
            }else if(editTextNewPassword.text.toString() != editTextConfirmPassword.text.toString()){
                createToast("Passwords are not the same")
            } else{
                if(databaseViewModel.changePassword(editTextNewPassword.text.toString())){
                    createToast("Password changed successfully")
                }else{
                    createToast("Server is grumpy, your password was not changed")
                }
                hideChangePasswordViews()
            }
        }

        buttonCancelPasswordChange.setOnClickListener{
            hideChangePasswordViews()
        }
    }

    fun createToast(text:String){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show()
    }

    fun hideChangePasswordViews(){
        buttonChangePwd.visibility = View.VISIBLE
        buttonCancelPasswordChange.visibility = View.GONE
        buttonSavePasswordChange.visibility = View.GONE
        editTextOldPassword.visibility = View.GONE
        editTextNewPassword.visibility = View.GONE
        editTextConfirmPassword.visibility = View.GONE
        editTextNewPassword.text.clear()
        editTextOldPassword.text.clear()
        editTextConfirmPassword.text.clear()
    }

    fun showChangePasswordViews(){
        buttonChangePwd.visibility = View.GONE
        editTextOldPassword.visibility = View.VISIBLE
        editTextNewPassword.visibility = View.VISIBLE
        editTextConfirmPassword.visibility = View.VISIBLE
        buttonSavePasswordChange.visibility = View.VISIBLE
        buttonCancelPasswordChange.visibility = View.VISIBLE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            profileImage.setImageURI(imageUri)
        }
    }
}