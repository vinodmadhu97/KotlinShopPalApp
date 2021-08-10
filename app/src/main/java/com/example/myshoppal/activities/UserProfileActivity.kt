package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshoppal.R
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var user = User()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_profle_fname.isEnabled = false
        et_profle_fname.setText(user.firstName)

        et_profile_LastName.isEnabled = false
        et_profile_LastName.setText(user.lastName)

        et_profile_Email.isEnabled = false
        et_profile_Email.setText(user.email)

    }
}