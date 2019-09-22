package com.dev452.library

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlurMaskFilter
import android.graphics.MaskFilter
import android.os.Build
import android.util.AttributeSet
import android.view.View

import com.dev452.library.ultimatetextview.UltimateTextView

/**
 * Created by Debojyoti Singha on 22,September,2019.
 */

open class BlurTextView : UltimateTextView {

    private var contextMain: Context? = null
    private val fetchedText: String? = null
    private var isBlurActive: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        this.contextMain = context

        if (attributeSet != null) {
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BlurTextView)
            isBlurActive = typedArray.getBoolean(R.styleable.BlurTextView_BTVIsBlurEnabled, false)
            settingBlur(this, isBlurActive)
            typedArray.recycle()
        }
    }

    private fun settingBlur(blurTextView: BlurTextView, isBlurActive: Boolean) {
        if (isBlurActive) {
            blurTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            val radius = blurTextView.textSize / 3
            val filter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
            blurTextView.paint.maskFilter = filter
        } else {
            blurTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            blurTextView.paint.maskFilter = null
        }
    }

    fun setBlurActive(blurActive: Boolean) {
        isBlurActive = blurActive
        settingBlur(this, blurActive)
    }
}
