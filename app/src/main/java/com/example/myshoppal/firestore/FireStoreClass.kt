package com.example.myshoppal.firestore

import android.app.Activity
import android.util.Log
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

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
    }
}