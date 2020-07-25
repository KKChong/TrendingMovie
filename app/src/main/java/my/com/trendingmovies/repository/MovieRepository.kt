package my.com.trendingmovies.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import my.com.trendingmovies.model.Movies
import my.com.trendingmovies.network.MovieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieService: MovieService
) {
    fun getTrendingMovie(): Single<Movies> {
        return movieService.getTrendingMovie()
            .subscribeOn(Schedulers.io())
    }
}