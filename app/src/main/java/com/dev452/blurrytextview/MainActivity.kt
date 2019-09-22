package com.dev452.blurrytextview

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.dev452.library.BlurTextView

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private var blurTextView: BlurTextView? = null
    private var blurBtn: Button? = null
    private var isClicked: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        blurBtn!!.setOnClickListener {
            isClicked = if (isClicked) {
                blurTextView!!.setBlurActive(true)
                blurBtn!!.text = "Deactivate Blur"
                false
            } else {
                blurTextView!!.setBlurActive(false)
                blurBtn!!.text = "Activate Blur"
                true
            }
        }
    }

    private fun init() {
        blurTextView = findViewById(R.id.blurTextView)
        blurBtn = findViewById(R.id.blurBtn)
        blurBtn!!.text = "Activate Blur"
    }
}
