package my.com.trendingmovies.network

import io.reactivex.rxjava3.core.Single
import my.com.trendingmovies.model.Cast
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.model.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("trending/all/day")
    fun getTrendingMovie(@Query("page") page: Int): Single<Movies>

    @GET("movie/{movieId}?append_to_response=credits")
    fun getMovie(@Path("movieId") movieId: Long): Single<Movie>

    @GET("person/{id}")
    fun getCastDetails(@Path("id") castId: Long): Single<Cast>
}