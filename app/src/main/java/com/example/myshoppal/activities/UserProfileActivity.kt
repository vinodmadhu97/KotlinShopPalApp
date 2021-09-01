package com.example.myshoppal.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var user: User
    private var mSelectedUri :Uri? = null
    private var mUserProfileImageUrl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        hideStatusBar()
        setupToolBar()

        user = User()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_profle_fname.isEnabled = false
        et_profle_fname.setText(user.firstName)

        et_profile_LastName.isEnabled = false
        et_profile_LastName.setText(user.lastName)

        et_profile_Email.isEnabled = false
        et_profile_Email.setText(user.email)

        iv_profile_pic.setOnClickListener(this)
        btn_profile_save.setOnClickListener(this)

    }

    private fun hideStatusBar(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    private fun setupToolBar(){
        setSupportActionBar(userProfileToolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        userProfileToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_profile_pic ->{
                    checkPermissions()
                }

                R.id.btn_profile_save ->{
                    showProgressDialog("Please wait!")
                    if (mSelectedUri != null){
                        FireStoreClass().uploadImageToCloudStorage(this,mSelectedUri!!,Constants.USER_PROFILE_IMAGE)
                    }else{
                        uploadUserProfileDetails()
                    }


                }
            }
        }
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(
                this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(this)
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                Constants.showImageChooser(this)
            }else{
                showSnackBar("storage permission denited",true)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        mSelectedUri  = data.data!!
                        GlideLoader(this)
                            .loadUserPicture(mSelectedUri!!,iv_profile_pic)
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"image loading failed",Toast.LENGTH_LONG).show()
                    }


                }
            }
        }
    }



    fun userprofileUpdateActivitySuccess(){
        hideProgressDialog()
        Toast.makeText(this,"profile updated",Toast.LENGTH_LONG).show()
        startActivity(Intent(this,DashBoardActivity::class.java))
        finish()
    }
    fun userprofileUpdateActivityFailed(){
        hideProgressDialog()
        Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()

    }

    fun userprofileUpLoadActivitySuccess(url:String){
        mUserProfileImageUrl = url
        uploadUserProfileDetails()
    }

    private fun uploadUserProfileDetails(){

        var userMap = HashMap<String,Any>()

        val mobileNo = et_profile_phone.text.toString().trim { it<= ' ' }
        val gender = if (radio_male.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }
        userMap[Constants.GENDER] = gender

        if (mobileNo.isNotEmpty()){
            userMap[Constants.PHONE] = mobileNo.toLong()
        }

        if (mUserProfileImageUrl.isNotEmpty()){
            userMap[Constants.IMAGE] = mUserProfileImageUrl
        }

        if (mobileNo.isNotEmpty() && gender.isNotEmpty() && mUserProfileImageUrl.isNotEmpty()){
            userMap[Constants.PROFILE_COMPLETED] = 1
        }


        FireStoreClass().updateUserProfileData(this,userMap)


    }
}