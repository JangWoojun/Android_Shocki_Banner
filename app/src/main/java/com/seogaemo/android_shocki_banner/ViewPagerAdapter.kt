package com.seogaemo.android_shocki_banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seogaemo.android_shocki_banner.databinding.BannerItemBinding

class ViewPagerAdapter(private val datas: MutableList<String>):
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        val binding = BannerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position % datas.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class ViewHolder(private val binding : BannerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String){
            binding.textView.text = item
            binding.image.setImageResource(R.drawable.banner6)
            binding.background.background.alpha = (0.4 * 255).toInt()
        }
    }
}