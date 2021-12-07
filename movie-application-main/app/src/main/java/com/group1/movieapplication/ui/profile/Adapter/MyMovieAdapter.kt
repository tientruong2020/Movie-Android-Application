package com.group1.movieapplication.ui.profile.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group1.movieapplication.R
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_movie_item_layout.view.*

class MyMovieAdapter(val context: Context) :
    RecyclerView.Adapter<MyMovieAdapter.MyMovieViewHolder>() {
    var myMovieList = ArrayList<IMDBMovieResponse>()
    var mClickListener: clickListener? = null


    interface clickListener {
        fun clickImage(movieID: String)
    }

    fun setClickListener(listenter: clickListener) {
        this.mClickListener = listenter
    }

    fun setData(myMovieArrlist: ArrayList<IMDBMovieResponse>) {
        this.myMovieList = myMovieArrlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMovieViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.my_movie_item_layout, parent, false)
        return MyMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyMovieViewHolder, position: Int) {
        val myMovie = myMovieList.get(position)
        holder.showData(myMovie)
    }

    override fun getItemCount(): Int {
        return myMovieList.size
    }

    inner class MyMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        fun showData(movieImg: IMDBMovieResponse) {
            val imgUri = Uri.parse(movieImg.image)
            Picasso.get().load(imgUri).into(itemView.imv_my_movie)
            itemView.tw_my_movie_title.text = movieImg.title
            itemView.imv_my_movie.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            when (v?.id) {
                (R.id.imv_my_movie) -> {
                    val position = bindingAdapterPosition
                    val movieID = myMovieList.get(position).id
                    if (position != RecyclerView.NO_POSITION) {
                        mClickListener?.clickImage(movieID)
                    }
                }
            }
        }
    }
}