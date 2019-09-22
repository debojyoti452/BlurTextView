package com.dev452.library.ultimatetextview.helper_class


import android.content.res.Resources
import android.os.Build
import android.text.Editable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.SingleLineTransformationMethod
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import com.dev452.library.R

import java.util.ArrayList

/**
 * Created by Debojyoti Singha on 30,May,2019.
 */
open class HelperForAutoFit private constructor(private val mBackBoneTextView: BackBoneTextView) {
    private val mPaint: TextPaint

    private var mTextSize: Float = 0.toFloat()
    private var mMaxLines: Int = 0
    private var mMinTextSize: Float = 0.toFloat()
    private var mMaxTextSize: Float = 0.toFloat()
    private var mPrecision = DEFAULT_PRECISION
    private var mEnabled: Boolean = false
    private var mIsAutofitting: Boolean = false
    private var mListeners: ArrayList<OnTextSizeChangeListener>? = null
    private val mTextWatcher = AutofitTextWatcher()

    var textSize: Float
        get() = mTextSize
        set(size) = setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

    init {
        val context = mBackBoneTextView.context
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        mPaint = TextPaint()
        setRawTextSize(mBackBoneTextView.textSize)

        mMaxLines = getMaxLines(mBackBoneTextView)
        mMinTextSize = scaledDensity * DEFAULT_MIN_TEXT_SIZE
        mMaxTextSize = mTextSize
        mPrecision = DEFAULT_PRECISION
    }

    fun addOnTextSizeChangeListener(listener: OnTextSizeChangeListener): HelperForAutoFit {
        if (mListeners == null) {
            mListeners = ArrayList()
        }
        mListeners!!.add(listener)
        return this
    }

    fun removeOnTextSizeChangeListener(listener: OnTextSizeChangeListener): HelperForAutoFit {
        if (mListeners != null) {
            mListeners!!.remove(listener)
        }
        return this
    }

    fun getPrecision(): Float {
        return mPrecision
    }

    fun setPrecision(precision: Float): HelperForAutoFit {
        if (mPrecision != precision) {
            mPrecision = precision

            AutoFitFunc()
        }
        return this
    }

    fun getMinTextSize(): Float {
        return mMinTextSize
    }

    fun setMinTextSize(size: Float): HelperForAutoFit {
        return setMinTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setMinTextSize(unit: Int, size: Float): HelperForAutoFit {
        val context = mBackBoneTextView.context
        var r = Resources.getSystem()

        if (context != null) {
            r = context.resources
        }

        setRawMinTextSize(TypedValue.applyDimension(unit, size, r.displayMetrics))
        return this
    }

    private fun setRawMinTextSize(size: Float) {
        if (size != mMinTextSize) {
            mMinTextSize = size

            AutoFitFunc()
        }
    }

    fun getMaxTextSize(): Float {
        return mMaxTextSize
    }

    fun setMaxTextSize(size: Float): HelperForAutoFit {
        return setMaxTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setMaxTextSize(unit: Int, size: Float): HelperForAutoFit {
        val context = mBackBoneTextView.context
        var r = Resources.getSystem()

        if (context != null) {
            r = context.resources
        }

        setRawMaxTextSize(TypedValue.applyDimension(unit, size, r.displayMetrics))
        return this
    }

    private fun setRawMaxTextSize(size: Float) {
        if (size != mMaxTextSize) {
            mMaxTextSize = size

            AutoFitFunc()
        }
    }

    fun getMaxLines(): Int {
        return mMaxLines
    }

    fun setMaxLines(lines: Int): HelperForAutoFit {
        if (mMaxLines != lines) {
            mMaxLines = lines

            AutoFitFunc()
        }
        return this
    }

    fun isEnabled(): Boolean {
        return mEnabled
    }

    fun setEnabled(enabled: Boolean): HelperForAutoFit {
        if (mEnabled != enabled) {
            mEnabled = enabled

            if (enabled) {
                mBackBoneTextView.addTextChangedListener(mTextWatcher)
                AutoFitFunc()
            } else {
                mBackBoneTextView.removeTextChangedListener(mTextWatcher)
                mBackBoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            }
        }
        return this
    }

    fun setTextSize(unit: Int, size: Float) {
        if (mIsAutofitting) {
            // We don't want to update the BackBoneTextView's actual textSize while we're autofitting
            // since it'd get set to the autofitTextSize
            return
        }
        val context = mBackBoneTextView.context
        var r = Resources.getSystem()

        if (context != null) {
            r = context.resources
        }

        setRawTextSize(TypedValue.applyDimension(unit, size, r.displayMetrics))
    }

    private fun setRawTextSize(size: Float) {
        if (mTextSize != size) {
            mTextSize = size
        }
    }

    private fun AutoFitFunc() {
        val oldTextSize = mBackBoneTextView.textSize
        val textSize: Float = mBackBoneTextView.textSize

        mIsAutofitting = true
        AutoFitFunc(mBackBoneTextView, mPaint, mMinTextSize, mMaxTextSize, mMaxLines, mPrecision)
        mIsAutofitting = false

        if (textSize != oldTextSize) {
            sendTextSizeChange(textSize, oldTextSize)
        }
    }

    private fun sendTextSizeChange(textSize: Float, oldTextSize: Float) {
        if (mListeners == null) {
            return
        }

        for (listener in mListeners!!) {
            listener.onTextSizeChange(textSize, oldTextSize)
        }
    }

    interface OnTextSizeChangeListener {
        fun onTextSizeChange(textSize: Float, oldTextSize: Float)
    }

    private inner class AutofitTextWatcher : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            // do nothing
        }

        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            AutoFitFunc()
        }

        override fun afterTextChanged(editable: Editable) {
            // do nothing
        }
    }

    companion object {

        private val TAG = "AutoFitTextHelper"
        private val SPEW = false

        private val DEFAULT_MIN_TEXT_SIZE = 8 //sp
        private val DEFAULT_PRECISION = 0.5f

        @JvmOverloads
        fun cookStuffs(
            view: BackBoneTextView,
            attrs: AttributeSet? = null,
            defStyle: Int = 0
        ): HelperForAutoFit {
            val helper = HelperForAutoFit(view)
            var sizeToFit = true
            if (attrs != null) {
                val context = view.context
                var minTextSize = helper.getMinTextSize().toInt()
                val precision = helper.getPrecision()

                val ta = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.UltimateTextView,
                    defStyle,
                    0
                )
                sizeToFit = ta.getBoolean(R.styleable.UltimateTextView_BTVAutoFitEnabled, sizeToFit)
                minTextSize = ta.getDimensionPixelSize(
                    R.styleable.UltimateTextView_BTVMinTextSize,
                    minTextSize
                )
                ta.recycle()

                helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize.toFloat())
                    .setPrecision(precision)
            }
            helper.setEnabled(sizeToFit)

            return helper
        }

        private fun AutoFitFunc(
            view: BackBoneTextView, paint: TextPaint, minTextSize: Float, maxTextSize: Float,
            maxLines: Int, precision: Float
        ) {
            if (maxLines <= 0 || maxLines == Integer.MAX_VALUE) {
                // Don't auto-size since there's no limit on lines.
                return
            }

            val targetWidth = view.width - view.paddingLeft - view.paddingRight
            if (targetWidth <= 0) {
                return
            }

            var text = view.text
            val method = view.transformationMethod
            if (method != null) {
                text = method.getTransformation(text, view)
            }

            val context = view.context
            var r = Resources.getSystem()
            val displayMetrics: DisplayMetrics

            var size = maxTextSize
            val high = size
            val low = 0f

            if (context != null) {
                r = context.resources
            }
            displayMetrics = r.displayMetrics

            paint.set(view.paint)
            paint.textSize = size

            if (maxLines == 1 && paint.measureText(
                    text,
                    0,
                    text.length
                ) > targetWidth || getLineCount(
                    text,
                    paint,
                    size,
                    targetWidth.toFloat(),
                    displayMetrics
                ) > maxLines
            ) {
                size = getAutoFitSize(
                    text, paint, targetWidth.toFloat(), maxLines, low, high, precision,
                    displayMetrics
                )
            }

            if (size < minTextSize) {
                size = minTextSize
            }

            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        }

        private fun getAutoFitSize(
            text: CharSequence, paint: TextPaint,
            targetWidth: Float, maxLines: Int, low: Float, high: Float, precision: Float,
            displayMetrics: DisplayMetrics
        ): Float {
            val mid = (low + high) / 2.0f
            var lineCount = 1
            var layout: StaticLayout? = null

            paint.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, mid,
                displayMetrics
            )

            if (maxLines != 1) {
                layout = StaticLayout(
                    text, paint, targetWidth.toInt(), Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0.0f, true
                )
                lineCount = layout.lineCount
            }

            if (SPEW)
                Log.d(
                    TAG, "low=" + low + " high=" + high + " mid=" + mid +
                            " target=" + targetWidth + " maxLines=" + maxLines + " lineCount=" + lineCount
                )

            if (lineCount > maxLines) {
                // For the case that `text` has more newline characters than `maxLines`.
                return if (high - low < precision) {
                    low
                } else getAutoFitSize(
                    text, paint, targetWidth, maxLines, low, mid, precision,
                    displayMetrics
                )
            } else if (lineCount < maxLines) {
                return getAutoFitSize(
                    text, paint, targetWidth, maxLines, mid, high, precision,
                    displayMetrics
                )
            } else {
                var maxLineWidth = 0f
                if (maxLines == 1) {
                    maxLineWidth = paint.measureText(text, 0, text.length)
                } else {
                    for (i in 0 until lineCount) {
                        if (layout!!.getLineWidth(i) > maxLineWidth) {
                            maxLineWidth = layout.getLineWidth(i)
                        }
                    }
                }

                return if (high - low < precision) {
                    low
                } else if (maxLineWidth > targetWidth) {
                    getAutoFitSize(
                        text, paint, targetWidth, maxLines, low, mid, precision,
                        displayMetrics
                    )
                } else if (maxLineWidth < targetWidth) {
                    getAutoFitSize(
                        text, paint, targetWidth, maxLines, mid, high, precision,
                        displayMetrics
                    )
                } else {
                    mid
                }
            }
        }

        private fun getLineCount(
            text: CharSequence, paint: TextPaint, size: Float, width: Float,
            displayMetrics: DisplayMetrics
        ): Int {
            paint.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics
            )
            val layout = StaticLayout(
                text, paint, width.toInt(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true
            )
            return layout.lineCount
        }

        private fun getMaxLines(view: BackBoneTextView): Int {
            var maxLines = -1 // No limit (Integer.MAX_VALUE also means no limit)

            val method = view.transformationMethod
            if (method != null && method is SingleLineTransformationMethod) {
                maxLines = 1
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // setMaxLines() and getMaxLines() are only available on android-16+
                maxLines = view.maxLines
            }

            return maxLines
        }
    }
}
