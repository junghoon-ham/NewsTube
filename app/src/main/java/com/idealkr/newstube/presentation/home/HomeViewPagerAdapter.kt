package com.idealkr.newstube.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idealkr.newstube.presentation.home.video_list.VideoListFragment

class HomeViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val channels: List<String>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun createFragment(position: Int): Fragment {
        return VideoListFragment()
    }
}