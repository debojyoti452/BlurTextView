package com.dev452.library.ultimatetextview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue

import com.dev452.library.ultimatetextview.helper_class.HelperForAutoFit
import com.dev452.library.ultimatetextview.helper_class.BackBoneTextView

/**
 * Created by Debojyoti Singha on 06,July,2019.
 */

open class UltimateTextView : BackBoneTextView, HelperForAutoFit.OnTextSizeChangeListener {
    var autofitHelper: HelperForAutoFit? = null
        private set

    private var mainContext: Context? = null

    var isSizeToFit: Boolean
        get() = autofitHelper!!.isEnabled()
        set(sizeToFit) {
            autofitHelper!!.setEnabled(sizeToFit)
        }

    var maxTextSize: Float
        get() = autofitHelper!!.getMaxTextSize()
        set(size) {
            autofitHelper!!.setMaxTextSize(size)
        }

    val minTextSize: Float
        get() = autofitHelper!!.getMinTextSize()

    var precision: Float
        get() = autofitHelper!!.getPrecision()
        set(precision) {
            autofitHelper!!.setPrecision(precision)
        }

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
        mainContext = context
        autofitHelper = HelperForAutoFit.cookStuffs(this, attrs, defStyleAttr)
            .addOnTextSizeChangeListener(this)
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        if (autofitHelper != null) {
            autofitHelper!!.setTextSize(unit, size)
        }
    }

    override fun setLines(lines: Int) {
        super.setLines(lines)
        if (autofitHelper != null) {
            autofitHelper!!.setMaxLines(lines)
        }
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        if (autofitHelper != null) {
            autofitHelper!!.setMaxLines(maxLines)
        }
    }

    fun setSizeToFit() {
        isSizeToFit = true
    }

    fun setMaxTextSize(unit: Int, size: Float) {
        autofitHelper!!.setMaxTextSize(unit, size)
    }

    fun setMinTextSize(minSize: Int) {
        autofitHelper!!.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, minSize.toFloat())
    }

    fun setMinTextSize(unit: Int, minSize: Float) {
        autofitHelper!!.setMinTextSize(unit, minSize)
    }

    override fun onTextSizeChange(textSize: Float, oldTextSize: Float) {
        // do nothing
    }
}
