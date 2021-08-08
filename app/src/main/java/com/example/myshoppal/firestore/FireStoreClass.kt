package com.example.myshoppal.firestore

import com.example.myshoppal.activities.SignUpActivity
import com.example.myshoppal.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity:SignUpActivity, user: User){
        mFireStore.collection("users").document(user.id).set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccessFull()
            }.addOnFailureListener {
                activity.hideProgressDialog()
            }
    }
}