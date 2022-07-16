package com.srcorp.mvcpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.srcorp.mvcpractice.model.Movie
import com.srcorp.mvcpractice.network.RetrofitClient

class MainAdapter(internal var moviList: List<Movie>,internal var context: Context):RecyclerView.Adapter<MainAdapter.MoviesHolder>() {
   val selectedMovies=HashSet<Movie>()
   inner class MoviesHolder(v:View):RecyclerView.ViewHolder(v) {
        internal var titleTextView:TextView
        internal var releaseDateTextView:TextView
        internal var movieImageView:ImageView
        internal var checkBox:CheckBox
        init {
            titleTextView=v.findViewById(R.id.title_textview)
            releaseDateTextView=v.findViewById(R.id.release_date_textview)
            movieImageView=v.findViewById(R.id.movie_imageview)
            checkBox=v.findViewById(R.id.checkbox)
            checkBox.setOnClickListener {
                val adapterPosition=adapterPosition
                if (!selectedMovies.contains(moviList[adapterPosition])){
                    checkBox.isChecked=true
                    selectedMovies.add(moviList[adapterPosition])
                }else{
                    checkBox.isChecked=false
                    selectedMovies.add(moviList[adapterPosition])
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val v=LayoutInflater.from(context).inflate(R.layout.item_movie_main,parent,false)
        return MoviesHolder(v)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.titleTextView.text = moviList[position].title
        holder.releaseDateTextView.text = moviList[position].releaseDate
        if (moviList[position].posterPath.equals("")) {
            holder.movieImageView.setImageDrawable(context.getDrawable(R.drawable.ic_local_movies_gray))
        } else {
            Picasso.get().load(RetrofitClient.TMDB_IMAGEURL + moviList[position].posterPath).into(holder.movieImageView)
        }
    }

    override fun getItemCount(): Int {
        return moviList.size
    }
}



























