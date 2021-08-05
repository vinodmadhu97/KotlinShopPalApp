package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextViewRegular(context: Context, attr: AttributeSet) : AppCompatTextView(context,attr) {
    init {
        applyFont()
    }

    private fun applyFont(){
        val typeface = Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}