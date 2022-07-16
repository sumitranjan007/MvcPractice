package com.srcorp.mvcpractice

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import com.srcorp.mvcpractice.databinding.ActivityAddMovieBinding
import com.srcorp.mvcpractice.model.LocalDataSource
import com.srcorp.mvcpractice.model.Movie
import com.srcorp.mvcpractice.network.RetrofitClient.TMDB_IMAGEURL

class AddMovieActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddMovieBinding
    lateinit var dataSource: LocalDataSource
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            this@AddMovieActivity.runOnUiThread{
                binding.apply {
                    movieTitle.setText(intent!!.getStringExtra(SearchActivity.EXTRA_TITLE))
                    movieReleaseDate.setText(intent.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE))
                    movieImageview.tag= intent.getStringExtra(SearchActivity.EXTRA_POSTER_PATH)
                    Picasso.get().load(TMDB_IMAGEURL+intent.getStringExtra(SearchActivity.EXTRA_POSTER_PATH)).into(movieImageview)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            dataSource= LocalDataSource(application)
        }
    }
    fun goToSearchMovieActivity(v: View){
        val title=binding.movieTitle.text.toString()
        val intent=Intent(this@AddMovieActivity,SearchActivity::class.java)
        intent.putExtra(SearchActivity.SEARCH_QUERY,title)
        startForResult.launch(intent)
    }
    fun onClickAddMovie(v:View){
        if (TextUtils.isEmpty(binding.movieTitle.text)){
            showToast("Movie title cannot be empty")
        }else{
            val title=binding.movieTitle.text.toString()
            val releaseDate=binding.movieReleaseDate.text.toString()
            val posterPath=if (binding.movieImageview.tag!=null) binding.movieImageview.tag.toString() else ""
            val movie=Movie(title = title, releaseDate = releaseDate, posterPath = posterPath)
            dataSource.insert(movie)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
    fun showToast(msg:String){
        Toast.makeText(this@AddMovieActivity,msg,Toast.LENGTH_SHORT).show()
    }
}





















