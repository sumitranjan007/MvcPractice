package com.srcorp.mvcpractice.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
    @SerializedName("vote_count")
    @Expose
    var voteCount:Int?=null,
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id:Int?=null,
    @SerializedName("video")
    @Expose
    var video:Boolean?=null,
    @SerializedName("vote_average")
    @Expose
    var voteAverage:Float?=null,
    @SerializedName("title")
    @Expose
    var title:String?=null,
    @SerializedName("popularity")
    @Expose
    var popularity:Float?=null,
    @SerializedName("poster_path")
    @Expose
    var posterPath:String?=null,
    @SerializedName("original_language")
    @Expose
    var originalLanguage:String?=null,
    @SerializedName("origional_title")
    @Expose
    var origionalTitle:String?=null,
    @SerializedName("gener_ids")
    @Expose
    var generIds:List<Int>?=null,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath:String?=null,
    @SerializedName("adult")
    @Expose
    var adult:Boolean?=null,
    @SerializedName("overview")
    @Expose
    var overview:String?=null,
    @SerializedName("release_date")
    @Expose
    var releaseDate:String?=null,
    var watched:Boolean=false

){
    fun getReleaseYearFromDate():String?{
        return releaseDate?.split("-")?.get(0)
    }
}
























