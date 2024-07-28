package com.seogaemo.android_shocki_banner

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.seogaemo.android_shocki_banner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewList: List<ConstraintLayout> by lazy {
        listOf(binding.banner1, binding.banner2, binding.banner3, binding.banner4, binding.banner5)
    }
    private val textList: List<TextView> by lazy {
        listOf(binding.banner1Text, binding.banner2Text, binding.banner3Text, binding.banner4Text, binding.banner5Text)
    }
    private val imageList: List<ImageView> by lazy {
        listOf(binding.banner1Image, binding.banner2Image, binding.banner3Image, binding.banner4Image, binding.banner5Image)
    }

    private var index = 0
    private var isLeft = true

    private val handler = Handler()
    private val runnable = object : Runnable {
        override fun run() {
            doSomething(isLeft)
            handler.postDelayed(this, 5000)
            isLeft = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(runnable)

        binding.bannerView.setOnTouchListener(object: OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                isLeft = true
                handler.removeCallbacks(runnable)
                handler.post(runnable)
            }
            override fun onSwipeRight() {
                isLeft = false
                handler.removeCallbacks(runnable)
                handler.post(runnable)
            }
        })
    }

    private fun setView(): Int {
        val viewWidth = if (this.applicationContext?.resources?.displayMetrics?.widthPixels != null) {
            (this.applicationContext?.resources?.displayMetrics?.widthPixels!! - dpToPx(32)) * 0.0488
        } else {
            0
        }.toInt()

        viewList.forEachIndexed { index, it ->
            val layoutParams = it.layoutParams as LinearLayout.LayoutParams
            layoutParams.width = viewWidth
            layoutParams.weight = 0f
            it.layoutParams = layoutParams

            textList[index].visibility = View.GONE

            imageList[index].setColorFilter(Color.parseColor("#80000000"))
        }

        return viewWidth
    }

    private fun dpToPx(dp: Int): Int {
        val density = this.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun doSomething(isLeft: Boolean) {
        val viewWidth = setView()
        val layoutParams = viewList[index].layoutParams as LinearLayout.LayoutParams
        layoutParams.width = viewWidth
        layoutParams.weight = 1f
        viewList[index].layoutParams = layoutParams

        textList[index].visibility = View.VISIBLE

        imageList[index].clearColorFilter()

        if (isLeft) {
            if (index+1 < 5) {
                index+=1
            } else {
                index = 0
            }
        } else {
            if (index-1 < 0) {
                index = 4
            } else {
                index-=1
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

}

