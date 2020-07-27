package my.com.trendingmovies.network

import io.reactivex.rxjava3.core.Single
import my.com.trendingmovies.model.Movies
import retrofit2.http.GET

interface MovieService {

    @GET("trending/all/day")
    fun getTrendingMovie(): Single<Movies>
}