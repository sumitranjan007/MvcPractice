package com.srcorp.mvcpractice

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.srcorp.mvcpractice.databinding.ActivityMainBinding
import com.srcorp.mvcpractice.model.LocalDataSource
import com.srcorp.mvcpractice.model.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
     lateinit var adapter:MainAdapter
     lateinit var dataSource:LocalDataSource
     private val compositeDisposable=CompositeDisposable()
    lateinit var binding:ActivityMainBinding
    private val TAG="mvc_p"
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            supportActionBar!!.title="Movies to watch"
             moviesRecyclerview.layoutManager=LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        dataSource= LocalDataSource(application)
        getMyMoviesList()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
    private fun getMyMoviesList(){
        val myMoviesDisposable=myMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(myMoviesDisposable)

    }
    private val myMoviesObservable:Observable<List<Movie>>
    get() = dataSource.allMovies

    private val observer:DisposableObserver<List<Movie>>
    get() = object:DisposableObserver<List<Movie>>(){
        override fun onNext(movieList: List<Movie>) {
            displayMovies(movieList)
        }

        override fun onError(e: Throwable) {
            Log.d(TAG,"Error$e")
            e.printStackTrace()
            displayError("Error fetching movie list")
        }

        override fun onComplete() {
            Log.d(TAG,"Compleated")
        }
    }
    fun displayMovies(movieList: List<Movie>?){
        if (movieList==null || movieList.size==0){
            Log.d(TAG,"No movies to display")
            binding.moviesRecyclerview.visibility=INVISIBLE
            binding.noMoviesLayout.visibility= VISIBLE
        }else{
            adapter= MainAdapter(movieList,this@MainActivity)
            binding.moviesRecyclerview.adapter=adapter
            binding.moviesRecyclerview.visibility=VISIBLE
            binding.noMoviesLayout.visibility= INVISIBLE
        }
    }
    fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_LONG).show()
    }

    fun displayError(e: String) {
        showToast(e)
    }
    //fab onClick
    fun goToAddMovieActivity(v: View) {
        val myIntent = Intent(this@MainActivity, AddMovieActivity::class.java)
        startForResult.launch(myIntent)
    }
}


















