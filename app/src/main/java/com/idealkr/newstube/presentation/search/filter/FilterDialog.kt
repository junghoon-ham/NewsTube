package com.idealkr.newstube.presentation.search.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.idealkr.newstube.R
import com.idealkr.newstube.databinding.FragmentFilterBinding
import com.idealkr.newstube.presentation.search.SearchViewModel
import com.idealkr.newstube.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class FilterDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by activityViewModels<SearchViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behaviour: BottomSheetBehavior<View>?
                behaviour = BottomSheetBehavior.from(view)

                behaviour.peekHeight = view.height
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindChannels()
        initObserve()
        setupListener()
    }

    private fun initObserve() {
        collectLatestStateFlow(searchViewModel.selectChannels) { channels ->
            with(binding) {
                buttonSearch.isEnabled = channels.isNotEmpty()
                buttonSearch.text = when (channels.isNotEmpty()) {
                    true -> getString(R.string.select_filter, channels.size)
                    false -> getString(R.string.please_select_filter)
                }
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            buttonClose.setOnClickListener {
                dismiss()
            }

            buttonReset.setOnClickListener {
                resetSelectChannels()
            }

            buttonSearch.setOnClickListener {
                runBlocking {
                    searchViewModel.setFilter()
                }

                dismiss()
            }
        }
    }

    private fun bindChannels() {
        searchViewModel.channelList.forEach {
            binding.flexboxLayout.addView(getChip(it))
        }
    }

    private fun getChip(channel: String): Chip {
        val chip = layoutInflater.inflate(R.layout.item_chip, binding.root, false) as Chip
        chip.apply {
            text = channel
            isChecked = searchViewModel.selectChannels.value.contains(channel)

            setOnClickListener {
                if (isChecked) {
                    searchViewModel.addChannel(channel)
                } else {
                    searchViewModel.removeChannel(channel)
                }
            }
        }
        return chip
    }

    private fun resetSelectChannels() {
        with(binding) {
            runBlocking {
                searchViewModel.removeAllChannel()
            }
            
            for (i in 0 until flexboxLayout.childCount) {
                val childView: View = flexboxLayout.getChildAt(i)
                if (childView is Chip) childView.isChecked = false
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}