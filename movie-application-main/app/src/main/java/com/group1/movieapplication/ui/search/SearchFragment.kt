package com.group1.movieapplication.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.group1.movieapplication.databinding.FragmentSearchBinding
import com.group1.movieapplication.model.search.Result
import com.group1.movieapplication.ui.search.adapter.TVSearchAdapter
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewmodel: SearchViewModel
    private lateinit var result: ArrayList<Result>
    private lateinit var tvSearchAdapter: TVSearchAdapter
    private var timer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.tvSearchRecyclerview.setHasFixedSize(true)
        viewmodel = ViewModelProvider(this)[SearchViewModel::class.java]
        result = ArrayList()
        tvSearchAdapter = TVSearchAdapter(result)
        tvSearchAdapter.setClickListener(object : TVSearchAdapter.ClickListener {
            override fun clickMovieItem(movieId: String) {
                val changeFragmentAction =
                    SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieId)
                findNavController().navigate(changeFragmentAction)
            }

        })
        binding.tvSearchRecyclerview.adapter = tvSearchAdapter
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer!!.cancel()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().isEmpty()) {
                    timer = Timer()
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post { getSearchTVshow(p0.toString()) }
                        }
                    }, 800)
                } else {
                    result.clear()
                    tvSearchAdapter.notifyDataSetChanged()
                }
            }

        })

    }

    private fun getSearchTVshow(query: String) {
        binding.isLoading = true
        viewmodel.getSearchTVShowRepository(query).observe(this, { searchtv ->
            binding.isLoading = false
            if (searchtv != null) {
                if (searchtv.getSearchitem() != null) {
                    val oldcount: Int = result.size
                    result.addAll(searchtv.getSearchitem())
                    for (x in result) {
                        println(x)
                    }
                    tvSearchAdapter.notifyItemRangeInserted(oldcount, result.size)
                }
            }
        })
    }

}