package com.idealkr.newstube.presentation.home.video_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idealkr.newstube.R
import com.idealkr.newstube.databinding.FragmentVideoListBinding
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.presentation.home.HomeFragmentDirections
import com.idealkr.newstube.util.Constants
import com.idealkr.newstube.util.collectLatestStateFlow
import com.idealkr.newstube.util.showSnackBar
import com.idealkr.newstube.util.toFormatDateString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListFragment : Fragment() {
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VideoViewModel>()

    private lateinit var videoAdapter: VideoPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoad()
        initObserve()
        setupListener()
        setupRecyclerView()
        setupLoadState()
    }

    private fun initLoad() {
        viewModel.getVideoList("아이폰", 1)
    }

    private fun initObserve() {
        collectLatestStateFlow(viewModel.getPagingResult) {
            videoAdapter.submitData(it)
        }
    }

    private fun setupListener() {
        with(binding) {
            fabScrollToTop.setOnClickListener {
                binding.recyclerView.smoothScrollToPosition(0)
            }

            refreshLayout.setOnRefreshListener {
                viewModel.getVideoList("아이폰", 1)
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoPagingAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            layoutManager = layout

            adapter = videoAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener(),
                RecyclerView.RecyclerListener {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val firstVisibleItemPosition = layout.findFirstVisibleItemPosition()
                    if (firstVisibleItemPosition >= 5) {
                        showFab()
                    } else {
                        hideFab()
                    }
                }

                override fun onViewRecycled(holder: RecyclerView.ViewHolder) {}
            })

            videoAdapter.setOnItemClickListener {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentWatch(
                    VideoInfo(
                        Constants.DUMMY_LIST.random(),
                        it.datetime?.toFormatDateString() ?: "",
                        it.displaySitename ?: "",
                        "KBS",
                        it.thumbnailUrl ?: ""
                    )
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun showFab() {
        binding.fabScrollToTop.run {
            if (!isVisible) {
                val animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                startAnimation(animation)
                isVisible = true
            }
        }
    }

    private fun hideFab() {
        binding.fabScrollToTop.run {
            if (isVisible) {
                val animation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                startAnimation(animation)
                isVisible = false
            }
        }
    }

    private fun setupLoadState() {
        videoAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = videoAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            //binding.textViewEmptyList.isVisible = isListEmpty
            binding.recyclerView.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            val errorState: LoadState.Error? = loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                binding.root.showSnackBar(it.error.message ?: "")
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}