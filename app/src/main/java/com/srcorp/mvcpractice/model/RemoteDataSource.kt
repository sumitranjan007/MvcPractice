package com.srcorp.mvcpractice.model

import com.srcorp.mvcpractice.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class RemoteDataSource {
    fun searchResultObservable(query:String):Observable<TmdbResponse>{
        return RetrofitClient.moviesApi
            .searchMovie(RetrofitClient.API_KEY,query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}