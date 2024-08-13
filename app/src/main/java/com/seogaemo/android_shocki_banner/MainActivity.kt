package com.seogaemo.android_shocki_banner

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.seogaemo.android_shocki_banner.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val indicatorList by lazy { listOf(binding.indicator0, binding.indicator1, binding.indicator2, binding.indicator3) }
    private val datas = mutableListOf("정성담아 키워낸,\n해남 황토 꿀고구마", "정성담아 키워낸,\n해남 황토 꿀고구마", "정성담아 키워낸,\n해남 황토 꿀고구마", "정성담아 키워낸,\n해남 황토 꿀고구마")

    private var myHandler = MyHandler()
    private val intervalTime = 1500.toLong()
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(0xffffff))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        binding.viewPager.apply {
            this.adapter = ViewPagerAdapter(datas)
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    indicatorList.forEach { it.setBackgroundResource(R.drawable.shape_circle_gray) }
                    val selectedIndicator = indicatorList[position % indicatorList.size]
                    selectedIndicator.setBackgroundResource(R.drawable.shape_circle_green)
                }
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                    }
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }

    private fun autoScrollStart(intervalTime: Long) {
        myHandler.removeMessages(0)
        myHandler.sendEmptyMessageDelayed(0, intervalTime)
    }

    private fun autoScrollStop(){
        myHandler.removeMessages(0)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            val statusBarHeight = statusBars.top
            val navBarHeight = systemBars.bottom

            binding.buttonLayout.setPadding(0, statusBarHeight, 0, navBarHeight)
            insets
        }
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if(msg.what == 0) {
                binding.viewPager.setCurrentItem(++currentPosition, true)
                autoScrollStart(intervalTime)
            }
        }
    }
}

