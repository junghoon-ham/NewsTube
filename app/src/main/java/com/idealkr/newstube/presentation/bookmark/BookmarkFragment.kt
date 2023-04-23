package com.idealkr.newstube.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.idealkr.newstube.databinding.FragmentBookmarkBinding
import com.idealkr.newstube.presentation.BaseFragment
import com.idealkr.newstube.util.collectLatestStateFlow
import com.idealkr.newstube.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : BaseFragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val viewModel by viewModels<BookmarkViewModel>()

    private lateinit var videoAdapter: BookmarkPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initObserve()
        setupLoadState()
    }

    private fun initObserve() {
        collectLatestStateFlow(viewModel.bookmarkPagingVideos) {
            videoAdapter.submitData(it)
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = BookmarkPagingAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            layoutManager = layout

            adapter = videoAdapter

            videoAdapter.setOnItemClickListener {
                val action = BookmarkFragmentDirections.actionFragmentBookmarkToFragmentWatch(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupLoadState() {
        videoAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = videoAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.textViewEmptyList.isVisible = isListEmpty
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