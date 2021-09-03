package com.example.myshoppal.firestore

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.Display
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myshoppal.activities.*
import com.example.myshoppal.fragments.DashboardFragment
import com.example.myshoppal.fragments.ProductsFragment
import com.example.myshoppal.models.Product
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity:SignUpActivity, user: User){
        mFireStore.collection(Constants.USERS).document(user.id).set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccessFull()
            }.addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun getCurrentUserId() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null){
            currentUserId = currentUser.uid
        }

        return currentUserId
    }

    fun getUserDetails(activity:Activity){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get()
            .addOnSuccessListener {
                Log.i("data",it.toString())
                val user = it.toObject(User::class.java)!!

                //SHARED PREFERENCES
                val sharedPreferences = activity.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES,Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString(Constants.LOGGED_IN_USERNAME,"${user.firstName} ${user.lastName}")
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity ->{
                        activity.profileDetailsSuccess(user)
                    }
                }
            }.addOnFailureListener{
                when(activity){
                    is LoginActivity ->{
                        activity.hideProgressDialog()
                        Toast.makeText(activity,"Something went wrong",Toast.LENGTH_LONG).show()
                    }
                    is SettingsActivity ->{
                        activity.hideProgressDialog()
                        Toast.makeText(activity,"Something went wrong",Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    fun updateUserProfileData(activity:Activity, userMap:HashMap<String,Any>){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).update(userMap).addOnSuccessListener {
            when(activity){
                is UserProfileActivity ->{
                    activity.userprofileUpdateActivitySuccess()
                }
            }
        }.addOnFailureListener {
            when(activity){
                is UserProfileActivity ->{
                    activity.userprofileUpdateActivityFailed()
                }
            }
        }

    }

    fun uploadImageToCloudStorage(activity: Activity,uri : Uri, imgType : String){
        var sRef :StorageReference = FirebaseStorage.getInstance().reference.child(
            imgType + System.currentTimeMillis()
        )

        sRef.putFile(uri).addOnSuccessListener {
            Log.i("data",it.metadata!!.reference!!.downloadUrl.toString())

            it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {savedPath->

                Log.i("data",savedPath.toString())
                when(activity){
                    is UserProfileActivity ->{
                        activity.userprofileUpLoadActivitySuccess(savedPath.toString())
                    }
                    is AddProductActivity ->{
                        activity.imageUploadSuccess(savedPath.toString())
                    }
                }
            }
        }.addOnFailureListener {
            when(activity){
                is UserProfileActivity ->{
                    activity.hideProgressDialog()
                }
            }

        }
    }

    fun signOutUser(){
        FirebaseAuth.getInstance().signOut()
    }

    fun uploadProductDetails(activity:Activity, productInfo: Product){
        mFireStore.collection(Constants.PRODUCT).document().set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
            when(activity){
                is AddProductActivity ->{
                    activity.productUploadSuccess()
                }
            }
        }
            .addOnFailureListener {
                when(activity){
                    is AddProductActivity ->{
                        activity.hideProgressDialog()
                        Log.i("data","prouduct details upload failed")
                    }
                }


            }
    }

    fun getProductList(fragment : Fragment){
        mFireStore.collection(Constants.PRODUCT).whereEqualTo(Constants.USER_ID,getCurrentUserId())
            .get().addOnSuccessListener { document ->
                Log.i("data",document.documents.toString())
                val dataList = ArrayList<Product>()

                for (i in document.documents){

                    var product = i.toObject(Product::class.java)
                    product!!.productId = i.id

                    dataList.add(product)
                }

                when(fragment){
                    is ProductsFragment ->{
                        fragment.successProductListFromFireStore(dataList)
                    }
                }

            }
    }

    fun getDashboardItemsList(fragment: DashboardFragment){
        mFireStore.collection(Constants.PRODUCT).get().addOnSuccessListener { document->
            Log.i("data","dashboard data ${document.documents.toString()}")

            val productList = ArrayList<Product>()
            for (i in document.documents){
                val product = i.toObject(Product::class.java)
                product!!.productId = i.id
                productList.add(product!!)
            }
            fragment.successDashboardItemList(productList)
        }.addOnFailureListener {
            fragment.hideProgressDialog()
            Log.i("data","dashboard data loading error")
        }
    }

    fun deleteProductItem(fragment:ProductsFragment,productId : String){
        mFireStore.collection(Constants.PRODUCT).document(productId).delete().addOnSuccessListener {
            fragment.productDeleteSuccess()
        }.addOnFailureListener {
            fragment.hideProgressDialog()
            Log.i("data"," data deleting error")
        }
    }


}