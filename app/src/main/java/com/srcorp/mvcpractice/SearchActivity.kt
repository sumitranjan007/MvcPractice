package com.srcorp.mvcpractice

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.srcorp.mvcpractice.databinding.ActivitySearchBinding
import com.srcorp.mvcpractice.model.RemoteDataSource
import com.srcorp.mvcpractice.model.TmdbResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class SearchActivity : AppCompatActivity() {
    private val TAG="SearchActivity"
    lateinit var query: String
    private lateinit var adapter:SearchcAdapter
    private var dataSource=RemoteDataSource()
    private val compositeDisposable= CompositeDisposable()
    lateinit var binding:ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        query=intent.getStringExtra(SEARCH_QUERY)!!
        binding.searchResultsRecyclerview.layoutManager=LinearLayoutManager(this@SearchActivity)
    }

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility=View.VISIBLE
        getSearchResults(query)
    }
   fun getSearchResults(query:String){
       val searchResultsDisposable=searchResultsObservable(query)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribeWith(observer)
       compositeDisposable.add(searchResultsDisposable)

   }
    val searchResultsObservable: (String) -> Observable<TmdbResponse> = { query -> dataSource.searchResultObservable(query) }

    val observer: DisposableObserver<TmdbResponse>
    get() = object:DisposableObserver<TmdbResponse>(){
        override fun onNext(tmdbResponse: TmdbResponse) {
            Log.d(TAG,"OnNext ${tmdbResponse.totalResults}")
            displayResult(tmdbResponse)
        }

        override fun onError(e: Throwable) {
            Log.d(TAG,"Error$e")
            e.printStackTrace()
            displayError("Error fetching Movie Data")
        }

        override fun onComplete() {
            Log.d(TAG,"Compleated")
        }

    }
    fun displayResult(tmdbResponse: TmdbResponse){
        binding.progressBar.visibility=View.GONE
        if (tmdbResponse.totalResults==null || tmdbResponse.totalResults==0){
            binding.searchResultsRecyclerview.visibility=View.GONE
            binding.noMoviesTextview.visibility=View.VISIBLE
        }else{
            adapter= SearchcAdapter(tmdbResponse.result?: arrayListOf(),this@SearchActivity,itemListenr)
            binding.searchResultsRecyclerview.adapter=adapter
            binding.searchResultsRecyclerview.visibility=View.VISIBLE
            binding.noMoviesTextview.visibility=View.GONE
        }
    }
    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
    //Listener for clicks on task in list view
    internal var itemListenr:RecyclerItemListener=object:RecyclerItemListener{
        override fun onItemClick(v: View, position: Int) {
            val movie=adapter.getItemAtPosition(position)
            val replyIntent=Intent()
            replyIntent.putExtra(EXTRA_TITLE,movie.title)
            replyIntent.putExtra(EXTRA_RELEASE_DATE,movie.getReleaseYearFromDate())
            replyIntent.putExtra(EXTRA_POSTER_PATH,movie.posterPath)
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }

    }

    interface RecyclerItemListener {
        fun onItemClick(v:View,position:Int)

    }
    companion object {

        val SEARCH_QUERY = "searchQuery"
        val EXTRA_TITLE = "SearchActivity.TITLE_REPLY"
        val EXTRA_RELEASE_DATE = "SearchActivity.RELEASE_DATE_REPLY"
        val EXTRA_POSTER_PATH = "SearchActivity.POSTER_PATH_REPLY"
    }
    fun showToast(string: String) {
        Toast.makeText(this@SearchActivity, string, Toast.LENGTH_LONG).show()
    }

    fun displayError(string: String) {
        showToast(string)
    }
}

















