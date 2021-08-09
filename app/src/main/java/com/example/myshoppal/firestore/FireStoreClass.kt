package com.example.myshoppal.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.Display
import com.example.myshoppal.activities.LoginActivity
import com.example.myshoppal.activities.SignUpActivity
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


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
                editor.apply()

                editor.putString(Constants.LOGGED_IN_USERNAME,"${user.firstName} ${user.lastName}")
                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
    }
}