package com.group1.movieapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.group1.movieapplication.databinding.FragmentHomeBinding
import com.group1.movieapplication.model.popular.PopularItem
import com.group1.movieapplication.model.upcoming.UpComingItem
import com.group1.movieapplication.ui.home.adapter.IMDBPopularEpoxyController
import com.group1.movieapplication.ui.home.adapter.IMDBUpcomingEpoxyController
import com.group1.movieapplication.ui.home.adapter.PopularItemOnClickListener
import com.group1.movieapplication.ui.home.adapter.UpcomingItemOnClickListener
import com.group1.movieapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private var isShowError = false

    private val viewModel by activityViewModels<HomeViewModel>()

    private val popularItemOnClickListener = object : PopularItemOnClickListener {
        override fun onClick(popularItem: PopularItem) {
            viewModel.selectedPopularItem = popularItem
            val action =
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(popularItem.id)
            findNavController().navigate(action)
        }
    }

    private val upcomingItemOnClickListener = object : UpcomingItemOnClickListener {
        override fun onClick(upComingItem: UpComingItem) {
            viewModel.selectedUpComingItem = upComingItem
            val action =
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(upComingItem.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        setupMovie()
        setupTv()
        setupUpcoming()
    }

    private fun setupMovie() {
        val movieEpoxyController = IMDBPopularEpoxyController(popularItemOnClickListener)
        binding.rvMovie.adapter = movieEpoxyController.adapter

        viewModel.movieList.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let {
                            binding.progressMovie.visibility = View.GONE
                            movieEpoxyController.setData(result.data.popularItems)
                        }
                    }
                    is NetworkResult.Loading -> {
                        binding.progressMovie.visibility = View.VISIBLE
                    }
                    else -> {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun setupTv() {
        val tvEpoxyController = IMDBPopularEpoxyController(popularItemOnClickListener)
        binding.rvTv.adapter = tvEpoxyController.adapter

        viewModel.tvList.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let {
                            binding.progressTv.visibility = View.GONE
                            tvEpoxyController.setData(result.data.popularItems)
                        }
                    }
                    is NetworkResult.Loading -> {
                        binding.progressTv.visibility = View.VISIBLE
                    }
                    else -> {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun setupUpcoming() {
        val upcomingEpoxyController = IMDBUpcomingEpoxyController(upcomingItemOnClickListener)
        binding.rvUpcoming.adapter = upcomingEpoxyController.adapter

        viewModel.upComingList.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let {
                            binding.progressUpcoming.visibility = View.GONE
                            upcomingEpoxyController.setData(result.data.upComingItems)
                        }
                    }
                    is NetworkResult.Loading -> {
                        binding.progressUpcoming.visibility = View.VISIBLE
                    }
                    else -> {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun showErrorDialog() {
        if (!isShowError) {
            isShowError = true
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setTitle("Loading Failed")
                .setMessage("Something is not right. Please check your internet and try again")
                .setPositiveButton("OK") { _, _ -> }

            dialogBuilder.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}