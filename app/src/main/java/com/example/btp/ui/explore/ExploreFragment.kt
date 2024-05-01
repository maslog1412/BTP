package com.example.btp.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btp.databinding.FragmentExploreBinding
import com.example.btp.ui.common.adapters.TouristAttractionAdapter
import com.example.btp.utils.Result
import com.example.btp.utils.onLoading
import com.example.btp.utils.onLoadingFailure

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var binding: FragmentExploreBinding
    private lateinit var touristAttractionAdapter: TouristAttractionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.destinationList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> touristAttractionAdapter.submitList(result.value)
            }
        }
    }

    private fun setupViews() {
        touristAttractionAdapter = TouristAttractionAdapter()
        binding.exploreDestinationRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = touristAttractionAdapter
        }
    }
}
