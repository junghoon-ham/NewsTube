package com.idealkr.newstube.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.idealkr.newstube.databinding.FragmentSearchBinding
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.presentation.BaseFragment
import com.idealkr.newstube.presentation.search.filter.FilterDialog
import com.idealkr.newstube.util.Constants.DUMMY_LIST
import com.idealkr.newstube.util.collectLatestStateFlow
import com.idealkr.newstube.util.showSnackBar
import com.idealkr.newstube.util.toFormatDateString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by activityViewModels<SearchViewModel>()

    private lateinit var searchAdapter: SearchPagingAdapter
    private lateinit var filterAdapter: FilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        setupRecyclerView()
        setupSearch()
        setupLoadState()
        setupClickListener()
    }

    private fun initObserve() {
        collectLatestStateFlow(searchViewModel.getPagingResult) {
            searchAdapter.submitData(it)
        }

        collectLatestStateFlow(searchViewModel.filterChannels) {
            searchViewModel.getVideoList("아아", 1)
            filterAdapter.setData(it)
            binding.constraintLayoutFilter.isVisible = it.isNotEmpty()
        }
    }

    private fun setupClickListener() {
        binding.buttonFilter.setOnClickListener {
            val dialog = FilterDialog()
            dialog.show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchPagingAdapter()
        filterAdapter = FilterAdapter(requireContext())

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter

            searchAdapter.setOnItemClickListener {
                val action = SearchFragmentDirections.actionFragmentSearchToFragmentWatch(
                    VideoInfo(
                        DUMMY_LIST.random(),
                        it.datetime?.toFormatDateString() ?: "",
                        it.displaySitename ?: "",
                        "KBS",
                        it.thumbnailUrl ?: ""
                    )
                )
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewFilter.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    private fun setupSearch() {
        with(binding) {
            textInputLayoutSearch.setEndIconOnClickListener {
                val text = editTextSearch.text.toString()
                searchViewModel.getVideoList(text, 1)
            }

            editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                val text = editTextSearch.text.toString()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewModel.getVideoList(text, 1)

                    editTextSearch.clearFocus()
                    val imm: InputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editTextSearch.windowToken, 0)

                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun setupLoadState() {
        searchAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = searchAdapter.itemCount < 1
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