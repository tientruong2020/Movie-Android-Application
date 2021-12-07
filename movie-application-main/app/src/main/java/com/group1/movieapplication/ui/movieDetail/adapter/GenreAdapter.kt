package com.group1.movieapplication.ui.movieDetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.group1.movieapplication.R

class GenreAdapter(var genreList: List<String>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(genre: String) {

            val genreButtonBinding = itemView.findViewById<MaterialButton>(R.id.genreBtn)
            genreButtonBinding.text = genre

            genreButtonBinding.setOnClickListener {
                var genre = genreButtonBinding.text.toString()

            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_detail_genre_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        holder.setData(genreList[position])
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
}