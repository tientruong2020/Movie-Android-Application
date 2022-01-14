package com.group1.movieapplication.ui.movieDetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group1.movieapplication.R
import com.group1.movieapplication.model.movieDetail.RatedMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_detail_viewer_rating_item.view.*

class RateAdapter:RecyclerView.Adapter<RateAdapter.RateViewholder>() {


    var rateList = emptyList<RatedMovie>()

    fun setData(list: ArrayList<RatedMovie>){
        this.rateList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_detail_viewer_rating_item, parent, false)
        return RateViewholder(view)
    }

    override fun onBindViewHolder(holder: RateViewholder, position: Int) {
        holder.bindData(rateList[position])
    }

    override fun getItemCount(): Int {
        if (rateList.isEmpty()){
            return 0
        }
        else{
            return rateList.size
        }
    }

    inner class RateViewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindData(ratedMovie: RatedMovie){
            itemView.userNameTv.text = ratedMovie.username
            itemView.userCmtTv.text = ratedMovie.comment
            itemView.userRatedBar.rating = ratedMovie.ratingScore!!.toFloat()
            Picasso.get().load(ratedMovie.userImage).into(itemView.userImageView)
        }
    }
}