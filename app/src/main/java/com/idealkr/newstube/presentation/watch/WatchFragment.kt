package com.idealkr.newstube.presentation.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.idealkr.newstube.R
import com.idealkr.newstube.databinding.FragmentWatchBinding
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.presentation.BaseFragment
import com.idealkr.newstube.presentation.home.video_list.VideoPagingAdapter
import com.idealkr.newstube.util.collectLatestStateFlow
import com.idealkr.newstube.util.showSnackBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WatchFragment : BaseFragment() {

    private var _binding: FragmentWatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WatchViewModel>()

    private val args by navArgs<WatchFragmentArgs>()
    private var videoInfo: VideoInfo? = null

    private lateinit var videoAdapter: VideoPagingAdapter

    private var startPage: Int? = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(binding.youtubePlayerView)

        try {
            videoInfo = args.video
        } catch (e: Exception) {

        }

        bindUI()
        setupListener()
        setupPlayer()
        initLoad()
        initObserve()
        setupRecyclerView()
    }

    private fun initLoad() {
        viewModel.getBookmarkVideo(videoInfo?.videoId ?: "")
        viewModel.getVideoList("아이폰", startPage ?: 1)
    }

    private fun initObserve() {
        collectLatestStateFlow(viewModel.getPagingResult) {
            videoAdapter.submitData(it)
        }

        collectLatestStateFlow(viewModel.isBookmarkVideo) {
            binding.imageViewBookmark.setImageResource(
                if (it) R.drawable.ic_baseline_bookmark_24 else R.drawable.ic_baseline_bookmark_border_24
            )
        }
    }

    private fun bindUI() {
        with(binding) {
            textViewChannel.text = videoInfo?.channel
            textViewTitle.text = videoInfo?.title
            textViewDate.text = videoInfo?.dateTime
        }
    }

    private fun setupListener() {
        with(binding) {
            imageViewBookmark.setOnClickListener { view ->
                if (videoInfo != null) {
                    if (viewModel.isBookmarkVideo.value) {
                        viewModel.deleteBookmarkVideo(videoInfo!!)
                        view.showSnackBar(getString(R.string.delete_bookmark))
                    } else {
                        viewModel.insertBookmarkVideo(videoInfo!!)
                        view.showSnackBar(getString(R.string.insert_bookmark))
                    }
                }
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

            videoAdapter.setOnItemClickListener {

            }
        }
    }

    private fun setupPlayer() {
        val listener = object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoInfo?.videoId ?: "", 0f)
            }
        }

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .build()

        binding.youtubePlayerView.initialize(listener, iFramePlayerOptions)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
