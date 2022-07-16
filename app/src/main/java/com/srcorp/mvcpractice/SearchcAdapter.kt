package com.srcorp.mvcpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.srcorp.mvcpractice.databinding.ItemMovieDetailsBinding
import com.srcorp.mvcpractice.model.Movie

class SearchcAdapter(var movieList: List<Movie>,var context: Context,var listener: SearchActivity.RecyclerItemListener):RecyclerView.Adapter<SearchcAdapter.ViewHolder>() {
    class ViewHolder (var binding:ItemMovieDetailsBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemMovieDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            titleTextview.text=movieList[position].title
            releaseDateTextview.text=movieList[position].getReleaseYearFromDate()
            overviewOverview.text=movieList[position].overview
            if (movieList[position].posterPath != null) {
                Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movieList[position].posterPath).into(movieImageview)
            }
            holder.itemView.setOnClickListener {
                  listener.onItemClick(it,position)
            }
        }
    }
    fun getItemAtPosition(pos: Int): Movie {
        return movieList[pos]
    }
    override fun getItemCount(): Int {
        return movieList.size
    }
}

































