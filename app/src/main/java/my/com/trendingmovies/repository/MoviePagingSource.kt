package my.com.trendingmovies.repository

import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.network.MovieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviePagingSource @Inject constructor(
    private val movieService: MovieService
) : RxPagingSource<Int, Movie>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val page = params.key ?: 1
        return movieService.getTrendingMovie(page)
            .subscribeOn(Schedulers.io())
            .map {
                LoadResult.Page(
                    data = it.results,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page == it.totalPages.toInt()) null else it.page.toInt() + 1
                ) as LoadResult<Int, Movie>
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

}