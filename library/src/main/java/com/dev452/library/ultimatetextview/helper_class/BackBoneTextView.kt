package com.dev452.library.ultimatetextview.helper_class

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.dev452.library.R

/**
 * Created by Debojyoti Singha on 06,July,2019.
 */
open class BackBoneTextView : AppCompatTextView {

    private var fontPath: String? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BackBoneTextView)
        if (typedArray != null) {
            fontPath = typedArray.getString(R.styleable.BackBoneTextView_BTVFonts)
            typedArray.recycle()
            setTypeFace(fontPath)
        }
    }

    fun setTypeFace(fontPath: String?) {
        if (fontPath != null) {
            val myTypeFace = Typeface.createFromAsset(this.context.assets, fontPath)
            this.typeface = myTypeFace
        }
    }
}
