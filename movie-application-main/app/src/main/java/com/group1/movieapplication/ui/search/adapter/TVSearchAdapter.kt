package com.group1.movieapplication.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group1.movieapplication.R
import com.group1.movieapplication.databinding.ItemSearchTvBinding
import com.group1.movieapplication.model.search.Result

class TVSearchAdapter(private val data: List<Result>) :
    RecyclerView.Adapter<TVSearchAdapter.TVSearchviewHolder>() {

    var mClickListener: ClickListener? = null

    interface ClickListener {
        fun clickMovieItem(movieId: String)
    }

    fun setClickListener(listener: ClickListener) {
        this.mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVSearchviewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val listitembinding = ItemSearchTvBinding.inflate(inflater, parent, false)
        return TVSearchviewHolder(listitembinding)
    }

    override fun onBindViewHolder(holder: TVSearchviewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class TVSearchviewHolder(val binding: ItemSearchTvBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Result) {
            binding.result = item
            binding.executePendingBindings()
            binding.movieItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            when (v?.id) {
                (R.id.movie_item_view) -> {
                    val dataItem = data.get(position)
                    if (position != RecyclerView.NO_POSITION) {
                        mClickListener?.clickMovieItem(dataItem.id)
                    }
                }
            }
        }
    }
}