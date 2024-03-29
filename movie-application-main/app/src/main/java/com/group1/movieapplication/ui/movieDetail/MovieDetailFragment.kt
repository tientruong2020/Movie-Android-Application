package com.group1.movieapplication.ui.movieDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.group1.movieapplication.R
import com.group1.movieapplication.databinding.MovieDetailLayoutBinding
import com.group1.movieapplication.model.movieDetail.Movie
import com.group1.movieapplication.model.movieDetail.RatedMovie
import com.group1.movieapplication.model.movieDetail.Trailer
import com.group1.movieapplication.ui.home.HomeViewModel
import com.group1.movieapplication.ui.movieDetail.adapter.GenreAdapter
import com.group1.movieapplication.ui.movieDetail.adapter.RateAdapter
import com.group1.movieapplication.ui.profile.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_detail_layout.*
import timber.log.Timber

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val movieViewModel by viewModels<MovieDetailViewModel>()
    private lateinit var rateAdapter: RateAdapter
    private val userViewModel by activityViewModels<ProfileViewModel>()

    private var rcvGenreAdapter: GenreAdapter? = null

    private var _binding: MovieDetailLayoutBinding? = null
    private val binding: MovieDetailLayoutBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailLayoutBinding.inflate(inflater, container, false)
        arguments?.let {
            val arg = MovieDetailFragmentArgs.fromBundle(it)
            Movie.movieId = arg.movieId
            Timber.d(arg.movieId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(Movie.movieId)
        getMovieDetail()
        getMovieTrailer()
        getMovieRating()
        setAction()
    }

    private fun getMovieDetail() {
        movieViewModel.getMovie()
        movieViewModel.movie.observe(viewLifecycleOwner) {
            if (it != null) {
                val movie =
                    Movie(it.title, it.type, it.year, it.summary, it.genre, it.rating + "/10")
                if (it.rating == "")
                    movie.rating = "N/A"
                binding.movieItem = movie
                Log.d("imgLink", it.image)
                setGenre(movie)
            }
        }
    }

    private fun getMovieTrailer() {
        movieViewModel.getTrailer()
        movieViewModel.trailer.observe(viewLifecycleOwner) {
            if (it != null) {
                val trailer = Trailer(it.videoUrl)
                binding.trailerItem = trailer
                setTrailer(trailer)
            }
        }
    }

    private fun getMovieRating() {
        val recyclerView:RecyclerView = binding.userRatingRecyclerView
        rateAdapter = RateAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = rateAdapter
        movieViewModel.getAllRating(Movie.movieId)
        movieViewModel.rating.observe(viewLifecycleOwner) { userRatingList ->
            if (userRatingList != null) {
                rateAdapter.setData(userRatingList)

            }
        }
    }

    private fun setGenre(movie: Movie) {

        val genreList: List<String> = movie.genre.split(", ")

        rcvGenreAdapter = GenreAdapter(genreList)
        val genreRecyclerView = view?.findViewById<RecyclerView>(R.id.genreHolder)
        genreRecyclerView?.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        genreRecyclerView?.adapter = rcvGenreAdapter

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setTrailer(trailer: Trailer) {
        val trailerPlayer = view?.findViewById<WebView>(R.id.trailerView)
        val url = "http://www.youtube.com/embed/" + trailer.trailerUrl + "?autoplay=1&vq=small"
        url.trim()

        trailerPlayer?.webViewClient = WebViewClient()
        trailerPlayer?.settings?.javaScriptEnabled = true
        trailerPlayer?.loadUrl(url)
        trailerPlayer?.webChromeClient = WebChromeClient()
    }


    private fun setAction() {
        val commentBtn = view?.findViewById<Button>(R.id.confBtn)
        val cancelBtn = view?.findViewById<Button>(R.id.cancelBtn)

        commentBtn?.setOnClickListener {
            saveRating()
        }

        cancelBtn?.setOnClickListener {
            resetComment()
        }
    }

    private fun saveRating() {
        userViewModel.getUserInfo().observe(viewLifecycleOwner) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val ratedMovie = RatedMovie(
                userId,
                userRatingBar.rating.toString(),
                userCmtEdt.text.toString()
            )
            movieViewModel.saveRating(ratedMovie).observe(viewLifecycleOwner,{
                if (it){
                    resetComment()
                }
            })
        }
    }

    private fun resetComment(){
        userRatingBar.rating = 0F
        userCmtEdt.text.clear()
    }
}