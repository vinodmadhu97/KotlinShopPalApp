package com.example.myshoppal.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.whenCreated
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.Product
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private var mImageUrl : Uri? = null
    private var mProductImageIrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        hideStatusBar()
        setupToolBar()

        iv_add_image.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)

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

        setSupportActionBar(add_product_action_bar)
        val toolbar = supportActionBar
        if (toolbar != null){
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
            toolbar.title = "Add Product"
            toolbar.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.toolbar_gradiant_background))
        }

        add_product_action_bar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_add_image ->{
                checkReadStoragePermission()
            }
            R.id.btn_submit_add_product -> {
                if (isValidateAddProductsDetails()){
                    uploadProductImage()
                }else{
                    showSnackBar("error",true);
                }
            }

        }
    }

    private fun checkReadStoragePermission(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(this)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        mImageUrl = data.data!!
                        GlideLoader(this).loadUserPicture(mImageUrl!!,product_image)
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"Image Loading failed.",Toast.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun isValidateAddProductsDetails() : Boolean{
        return when {

            mImageUrl == null ->{
                showSnackBar("please select product image",true)
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) ->{
                showSnackBar("please enter product title", true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) ->{
                showSnackBar("please enter product price",true)
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) ->{
                showSnackBar("please enter product description",true)
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it <= ' ' }) ->{
                showSnackBar("please enter number of quantity",true)
                false
            }

            else -> true
        }
    }

    private fun uploadProductImage(){
        showProgressDialog("please wait")
        FireStoreClass().uploadImageToCloudStorage(this,mImageUrl!!,Constants.PRODUCT_IMAGE)
    }

    fun imageUploadSuccess(savedPath:String){
        //hideProgressDialog()
        //showSnackBar("image uploaded",false)
        mProductImageIrl = savedPath


        uploadProductDetails()
    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME,"")
        val product = Product(
            FireStoreClass().getCurrentUserId(),
            username!!,
            et_product_title.text.toString().trim{it <= ' '},
            et_product_price.text.toString().trim{it <= ' '},
            et_product_description.text.toString().trim{it <= ' '},
            et_product_quantity.text.toString().trim{it <= ' '},
            mProductImageIrl
        )

        FireStoreClass().uploadProductDetails(this,product)
    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Product Details Added",Toast.LENGTH_LONG).show()
        finish()
    }
}