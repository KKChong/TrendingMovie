package my.com.trendingmovies.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import my.com.trendingmovies.model.Cast
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.network.MovieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val moviePagingSource: MoviePagingSource
) {
    fun getTrendingMovie(): Flowable<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { moviePagingSource }
        ).flowable
    }

    fun getMovieDetails(movieId: Long): Single<Movie> {
        return movieService.getMovie(movieId)
            .subscribeOn(Schedulers.io())
    }

    fun getCastDetails(castId: Long): Single<Cast> {
        return movieService.getCastDetails(castId)
            .subscribeOn(Schedulers.io())
    }
}