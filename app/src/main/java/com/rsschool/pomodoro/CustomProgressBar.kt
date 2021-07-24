package com.rsschool.pomodoro

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RotateDrawable
import android.util.AttributeSet
import android.widget.ProgressBar

class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttrs: Int = 0
): ProgressBar(context, attrs, defaultAttrs) {

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0).apply {
            try {
                val shouldSetColor = getString(R.styleable.CustomProgressBar_custom_color)
                if (shouldSetColor != null) {
                    val drawable = progressDrawable
                    val layerDrawable = drawable as LayerDrawable
                    val rotateDrawable = layerDrawable.findDrawableByLayerId(R.id.item_layer) as RotateDrawable
                    val shapeDrawable = rotateDrawable.drawable as GradientDrawable
                    // shapeDrawable.getDrawable(R.drawable.progress_shape)

                    shapeDrawable.setColor(Color.parseColor(shouldSetColor))
//                    if (shapeDrawable is ColorDrawable){
//                        shapeDrawable.color = R.styleable.CustomProgressBar_custom_color
//                    }
                }
            } finally {
                recycle()
            }
        }
    }
}