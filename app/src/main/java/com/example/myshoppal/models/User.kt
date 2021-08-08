package com.example.myshoppal.models

data class User (
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val image : String = "",
    val mobile : Long = 0,
    val gender : String = "",
    val isProfileCompleted : Int = 0
        )