package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.resources.CancelableFontCallback

class MSPButton(context: Context,attr:AttributeSet) : AppCompatButton(context,attr) {
    init {
        applyFont()
    }

    private fun applyFont(){
        val typeface = Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}