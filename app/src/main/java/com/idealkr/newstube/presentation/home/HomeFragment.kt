package com.idealkr.newstube.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.idealkr.newstube.databinding.FragmentHomeBinding
import com.idealkr.newstube.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoad()
        initObserve()
    }

    private fun initLoad() {
        runBlocking {
            viewModel.getChannelList()
            setupViewPager(viewModel.channelList.value)
            setupTabLayout(viewModel.channelList.value)
        }

    }

    private fun initObserve() {
        //collectLatestStateFlow(viewModel.channelList) { channels ->
        //    setupViewPager(channels)
        //    setupTabLayout(channels)
        //}
    }

    private fun setupViewPager(channels: List<String>) {
        with(binding) {
            viewPager.isSaveEnabled = false
            val adapter =
                HomeViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, channels)
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = channels.size / 2
        }
    }

    private fun setupTabLayout(channels: List<String>) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = channels[position]
        }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}