package com.example.teamprojmobv.views

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teamprojmobv.data.util.Injection
import androidx.navigation.findNavController
import com.example.teamprojmobv.Data.util.Injection
import com.example.teamprojmobv.R
import com.example.teamprojmobv.databinding.FragmentProfileBinding
import com.example.teamprojmobv.views.viewModels.DatabaseViewModel
import com.example.viewmodel.data.db.model.UserItem
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userInfo : UserItem
    private val pickImage = 100


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

        userInfo = databaseViewModel.getUserInfo()
        setUpProfileData(userInfo)
        setUpListenersForProfileImage()
        setListenersForChangingPassword()
        if(databaseViewModel.getImageUri() == null){
            loadProfilePictureFromServer(userInfo.profile)
        }else{
            profileImage.setImageURI(databaseViewModel.getImageUri())
        }
        setListenersForLogOut(view)
    }

    fun loadProfilePictureFromServer(endPoint : String){
        val imageUrl2 = "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg"
        val imageUrl = "http://api.mcomputing.eu/mobv/uploads/"+endPoint
        Log.i("profilefragment" , imageUrl)
        val profileImage = view?.findViewById<ImageView>(R.id.profileImage)
        Picasso.get().isLoggingEnabled = true
        Picasso.get()
            .load(imageUrl)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .placeholder(R.drawable.ic_profile_pic)
            .into(profileImage)
    }

    fun setUpProfileData(userInfo : UserItem){
        //TODO tu by som mala spristupnovat local cache ale narychlo som to nevedela
        binding.nickname = userInfo.username
        binding.mail = userInfo.email
    }

    fun setUpListenersForProfileImage(){
        profileImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        imageEditIcon.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    fun setListenersForChangingPassword(){
        buttonChangePwd.setOnClickListener{
            showViewsForChangingPassword()
        }
        buttonSavePasswordChange.setOnClickListener{
            checkIfPasswordsAreInCorrectForm()
        }
        buttonCancelPasswordChange.setOnClickListener{
            hideViewsForChangingPassword()
        }
    }

    fun setListenersForLogOut(view : View){
        buttonLogout.setOnClickListener(){
            databaseViewModel.logOutUser()
            view.findNavController().navigate(R.id.action_profileFragment_to_titleFragment)
        }
    }

    fun checkIfPasswordsAreInCorrectForm(){
        //TODO tu by som mala spristupnovat local cache ale narychlo som to nevedela
        //na mysli mam databaseViewModel.getCurrentPassword()
        if(editTextOldPassword.text.toString() != databaseViewModel.getCurrentPassword()){
            createToast("Incorrect old password")
            editTextOldPassword.text.clear()
        }else if(editTextNewPassword.text.toString() == "" || editTextConfirmPassword.text.toString() == ""){
            createToast("Fields cannot be empty")
        } else if(editTextOldPassword.text.toString() == editTextNewPassword.text.toString()){
            createToast("Old and new passwords are the same")
        }else if(editTextNewPassword.text.toString() != editTextConfirmPassword.text.toString()){
            createToast("Passwords are not the same")
        }else if(editTextNewPassword.text.toString().length < 4 || editTextNewPassword.text.toString().length > 32){
            createToast("Password needs to have min 4 and max 32 characters.")
        } else{
            if(databaseViewModel.changePassword(editTextNewPassword.text.toString())){
                createToast("Password changed successfully")
            }else{
                createToast("Server is grumpy, your password was not changed")
            }
            hideViewsForChangingPassword()
        }
    }

    fun createToast(text:String){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show()
    }

    fun hideViewsForChangingPassword(){
        buttonChangePwd.visibility = View.VISIBLE
        buttonCancelPasswordChange.visibility = View.GONE
        buttonSavePasswordChange.visibility = View.GONE
        editTextOldPassword.visibility = View.GONE
        editTextNewPassword.visibility = View.GONE
        editTextConfirmPassword.visibility = View.GONE
        editTextNewPassword.text.clear()
        editTextConfirmPassword.text.clear()
        editTextOldPassword.text.clear()
    }

    fun showViewsForChangingPassword(){
        buttonChangePwd.visibility = View.GONE
        editTextOldPassword.visibility = View.VISIBLE
        editTextNewPassword.visibility = View.VISIBLE
        editTextConfirmPassword.visibility = View.VISIBLE
        buttonSavePasswordChange.visibility = View.VISIBLE
        buttonCancelPasswordChange.visibility = View.VISIBLE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var selectedImageUri: Uri? = null


        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            selectedImageUri = data?.data
            profileImage.setImageURI(selectedImageUri)
            if (selectedImageUri != null) {
                databaseViewModel.setImageUri(selectedImageUri)
            }

            //nechce to stahovat zo servera fotku ktoru ma - ako keby si to
            //pamatalo to co stiahlo ako prve
            val picturePath = getPicturePath(selectedImageUri)
            if (picturePath != null) {
                if(!databaseViewModel.loadUserPhoto(picturePath)){
                    createToast("There was a problem uploading the picture, try again")
                }
            }
        }
    }

    fun getPicturePath(selectedImageUri : Uri?):String?{
        val filePathColumn =
            arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor? = selectedImageUri?.let {
            activity?.contentResolver?.query(
                it,
                filePathColumn, null, null, null
            )
        }
        cursor?.moveToFirst()

        val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath: String? = columnIndex?.let { cursor?.getString(it) }
        cursor?.close()
        return picturePath
    }
}