package com.seogaemo.android_shocki_banner

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.seogaemo.android_shocki_banner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var bannerWidth: Int? = null

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
            updateView()
            handler.postDelayed(this, 5000)
            isLeft = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        handler.postDelayed(runnable, 5000)

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

    private fun initView() {
        val viewWidth = dpToPx(16)

        viewList.forEachIndexed { index, it ->
            if (index == 0) {
                val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                layoutParams.width = viewWidth
                layoutParams.weight = 1f
                it.layoutParams = layoutParams

                textList[index].visibility = View.VISIBLE
                imageList[index].clearColorFilter()

                calculateWidth(it)
            } else {
                val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                layoutParams.width = viewWidth
                layoutParams.weight = 0f
                it.layoutParams = layoutParams

                textList[index].visibility = View.INVISIBLE
                imageList[index].setColorFilter(Color.parseColor("#80000000"))
            }
        }
    }

    private fun updateView() {
        val previousViewWidth = dpToPx(16)
        bannerWidth?.let { nextViewWidth ->
            val previousView = viewList[index]
            val previousLayoutParams = previousView.layoutParams as LinearLayout.LayoutParams
            val previousAnimator = ValueAnimator.ofInt(nextViewWidth, previousViewWidth)
            previousAnimator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                previousLayoutParams.width = animatedValue
                previousView.layoutParams = previousLayoutParams
            }
            previousAnimator.duration = 300
            previousAnimator.start()

            textList[index].visibility = View.INVISIBLE
            imageList[index].setColorFilter(Color.parseColor("#80000000"))

            updateIndex()

            val nextView = viewList[index]
            val nextLayoutParams = nextView.layoutParams as LinearLayout.LayoutParams
            val nextAnimator = ValueAnimator.ofInt(previousViewWidth, nextViewWidth)
            nextAnimator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                if (animatedValue == nextViewWidth) {
                    textList[index].visibility = View.VISIBLE
                }
                imageList[index].clearColorFilter()
                nextLayoutParams.width = animatedValue
                nextView.layoutParams = nextLayoutParams
            }
            nextAnimator.duration = 300
            nextAnimator.start()
        }
    }

    private fun updateIndex() {
        if (isLeft) {
            if (index + 1 < 5) {
                index += 1
            } else {
                index = 0
            }
        } else {
            if (index - 1 < 0) {
                index = 4
            } else {
                index -= 1
            }
        }
    }

    private fun calculateWidth(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                bannerWidth = view.width
            }
        })
    }

    private fun dpToPx(dp: Int): Int {
        val density = this.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

}

