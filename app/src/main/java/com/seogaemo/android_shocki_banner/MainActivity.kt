package com.seogaemo.android_shocki_banner

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(0xffffff))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        binding.viewPager.apply {
            this.adapter = ViewPagerAdapter(datas)
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(p0: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }
                override fun onPageSelected(p0: Int) {
                    indicatorList.forEach { it.setBackgroundResource(R.drawable.shape_circle_gray) }
                    when(p0){
                        0 -> indicatorList[0].setBackgroundResource(R.drawable.shape_circle_green)
                        1 -> indicatorList[1].setBackgroundResource(R.drawable.shape_circle_green)
                        2 -> indicatorList[2].setBackgroundResource(R.drawable.shape_circle_green)
                        3 -> indicatorList[3].setBackgroundResource(R.drawable.shape_circle_green)
                        else -> indicatorList[1].setBackgroundResource(R.drawable.shape_circle_green)
                    }
                }
            })
        }

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

}

